--
-- PostgreSQL database dump
--

-- Dumped from database version 11.1
-- Dumped by pg_dump version 11.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: dbmanager
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO dbmanager;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: dbmanager
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.roles OWNER TO dbmanager;

--
-- Name: spring_session; Type: TABLE; Schema: public; Owner: dbmanager
--

CREATE TABLE public.spring_session (
    primary_id character(36) NOT NULL,
    session_id character(36) NOT NULL,
    creation_time bigint NOT NULL,
    last_access_time bigint NOT NULL,
    max_inactive_interval integer NOT NULL,
    expiry_time bigint NOT NULL,
    principal_name character varying(100)
);


ALTER TABLE public.spring_session OWNER TO dbmanager;

--
-- Name: spring_session_attributes; Type: TABLE; Schema: public; Owner: dbmanager
--

CREATE TABLE public.spring_session_attributes (
    session_primary_id character(36) NOT NULL,
    attribute_name character varying(200) NOT NULL,
    attribute_bytes bytea NOT NULL
);


ALTER TABLE public.spring_session_attributes OWNER TO dbmanager;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: dbmanager
--

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    user_email character varying(255) NOT NULL,
    role_id bigint NOT NULL,
    role_name character varying(255) NOT NULL
);


ALTER TABLE public.user_roles OWNER TO dbmanager;

--
-- Name: users; Type: TABLE; Schema: public; Owner: dbmanager
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    bet_balance bigint,
    email character varying(255) NOT NULL,
    email_confirmed boolean,
    enabled boolean,
    free_balance bigint,
    os character varying(255),
    password character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    withdrawal_balance bigint
);


ALTER TABLE public.users OWNER TO dbmanager;

--
-- Name: users_verification_code; Type: TABLE; Schema: public; Owner: dbmanager
--

CREATE TABLE public.users_verification_code (
    id bigint NOT NULL,
    attemps integer,
    check_code integer,
    create_date timestamp without time zone,
    delay_date timestamp without time zone,
    user_email character varying(255),
    expired_date timestamp without time zone
);


ALTER TABLE public.users_verification_code OWNER TO dbmanager;

--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: spring_session_attributes spring_session_attributes_pk; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_pk PRIMARY KEY (session_primary_id, attribute_name);


--
-- Name: spring_session spring_session_pk; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.spring_session
    ADD CONSTRAINT spring_session_pk PRIMARY KEY (primary_id);


--
-- Name: users uk_5wpcaw3c6uf9qbbb8avvfh3xm; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_id_email UNIQUE (id, email);


--
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_email UNIQUE (email);


--
-- Name: users uk_du5v5sr43g5bfnji4vb8hg5s3; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_phone UNIQUE (phone);


--
-- Name: roles uk_k119kaqyutqcshswwho01bs9e; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uk_id_name UNIQUE (id, name);


--
-- Name: roles uk_ofx66keruapi6vyqpv6f2or37; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uk_name UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users_verification_code users_verification_code_pkey; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.users_verification_code
    ADD CONSTRAINT users_verification_code_pkey PRIMARY KEY (id);


--
-- Name: spring_session_ix1; Type: INDEX; Schema: public; Owner: dbmanager
--

CREATE UNIQUE INDEX spring_session_ix1 ON public.spring_session USING btree (session_id);


--
-- Name: spring_session_ix2; Type: INDEX; Schema: public; Owner: dbmanager
--

CREATE INDEX spring_session_ix2 ON public.spring_session USING btree (expiry_time);


--
-- Name: spring_session_ix3; Type: INDEX; Schema: public; Owner: dbmanager
--

CREATE INDEX spring_session_ix3 ON public.spring_session USING btree (principal_name);


--
-- Name: spring_session_attributes spring_session_attributes_fk; Type: FK CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_fk FOREIGN KEY (session_primary_id) REFERENCES public.spring_session(primary_id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

