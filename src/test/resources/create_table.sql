create schema cqc;
set search_path to courses;

create table cqc_elem_dict
(
    id   uuid         not null
        unique,
    name varchar(150) not null
        constraint cqc_elem_dict_pk
            primary key
);

create table cqc_elem
(
    id        uuid         not null
        constraint cqc_element_pk
            primary key,
    parent_id uuid
        constraint parent_id_fk
            references cqc_elem
            on delete cascade,
    type      uuid         not null
        constraint type_id_fk
            references cqc_elem_dict (id)
            on delete cascade,
    value     varchar(250) not null
);

create table cqc_elem_hierarchy
(
    child_type  uuid not null
        constraint child_fk
            references cqc_elem_dict (id)
            on delete cascade,
    parent_type uuid not null
        constraint parent_fk
            references cqc_elem_dict (id)
            on delete cascade,
    constraint child_parent_pk
        primary key (child_type, parent_type)
);

create table course
(
    id   uuid         not null
        constraint course_pk
            primary key,
    name varchar(250) not null
);

create table course_input_leaf_link
(
    course_id uuid not null
        constraint course_input_leaf_fk
            references course
            on delete cascade,
    leaf_id   uuid not null
        constraint input_leaf_fk
            references cqc_elem
            on delete cascade
);

create table course_output_leaf_link
(
    course_id uuid not null
        constraint course_output_leaf_fk
            references course
            on delete cascade,
    leaf_id   uuid not null
        constraint output_leaf_fk
            references cqc_elem
            on delete cascade
);

create function course_leaf_insert_trigger() returns trigger
    language plpgsql
as
$$
BEGIN
    IF ((SELECT parent_type
         FROM cqc_elem_hierarchy
         WHERE parent_type =
               (SELECT type FROM cqc_elem WHERE id = NEW.leaf_id)
         LIMIT 1) IS NOT NULL) THEN
        RAISE EXCEPTION 'invalid relationship';
    END IF;
    RETURN NEW;
END;
$$;

create function cqc_elem_insert_trigger() returns trigger
    language plpgsql
as
$$
DECLARE
    parentType uuid;
    temp       uuid;

BEGIN
    IF (NEW.parent_id IS NOT NULL) THEN
        parentType := (SELECT cqc.cqc_elem.type
                       FROM cqc.cqc_elem
                       WHERE id = NEW.parent_id);

        temp := (SELECT cqc.cqc_elem_hierarchy.parent_type
                 FROM cqc.cqc_elem_hierarchy
                 WHERE child_type = NEW.type);

        IF (temp IS NULL OR parentType <> temp) THEN
            RAISE EXCEPTION 'invalid relationship';
        END IF;
    END IF;
    RETURN NEW;
END ;
$$;

create or replace function get_course_parts(courseid uuid)
    returns TABLE(id uuid, parent_id uuid, type uuid, value character varying)
    language plpgsql
as
$$
DECLARE
    temp UUID;
    res  UUID[];
    leafArr UUID[];

BEGIN
    CREATE TEMP TABLE steps
    (
        stepNumber SERIAL,
        step       UUID[]
    );

    leafArr := (SELECT id FROM cqc_elem
                                   LEFT JOIN course_output_leaf_link ON id = leaf_id
                WHERE courseId = course_id);

    INSERT INTO steps(step)
    VALUES (leafArr);

    res := leafArr;

    WHILE (NOT ((SELECT step FROM steps ORDER BY stepNumber DESC LIMIT 1) = '{}'))
        LOOP
            DECLARE
                tempArr UUID[];

            BEGIN
                FOREACH temp IN ARRAY (SELECT step FROM steps ORDER BY stepNumber DESC LIMIT 1)
                    LOOP
                        tempArr := ARRAY(SELECT cqc_elem.parent_id
                                         FROM cqc_elem
                                         WHERE cqc_elem.parent_id IS NOT NULL
                                           AND cqc_elem.id = temp);

                        res := array_cat(res, tempArr);
                        INSERT INTO steps(step) VALUES (tempArr);
                    END LOOP;
            END;
        END LOOP;
    DROP TABLE steps;

    -- Чтобы из массива убрать дубликаты его нужно развернуть в таблицу с одним столбцом
    -- сделать по этой таблице DISTINCT и результат обратно собрать в массив
    RETURN QUERY (SELECT * FROM cqc_elem WHERE cqc_elem.id IN (SELECT DISTINCT r FROM unnest(res) AS result(r)));
END;
$$;

