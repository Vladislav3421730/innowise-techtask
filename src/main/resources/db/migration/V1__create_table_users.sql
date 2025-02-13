CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE public.users
(
    id         BIGINT PRIMARY KEY DEFAULT nextval('user_id_seq'),
    first_name VARCHAR(255) NOT NULL CHECK (first_name !~ '[0-9]'),
    last_name  VARCHAR(255) NOT NULL CHECK (last_name !~ '[0-9]'),
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) UNIQUE CHECK (POSITION('@' IN email) > 1),
    birthday   DATE NOT NULL CHECK (birthday <= CURRENT_DATE)
);

ALTER TABLE public.users OWNER TO postgres;
ALTER SEQUENCE public.user_id_seq OWNED BY public.users.id;





