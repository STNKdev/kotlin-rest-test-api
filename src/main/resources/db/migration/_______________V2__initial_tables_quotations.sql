--
-- Name: instrument; Type: TABLE; Schema: public; Owner: dbmanager
--

CREATE TABLE public.instrument (
                                   id bigint NOT NULL,
                                   created_at timestamp without time zone NOT NULL,
                                   updated_at timestamp without time zone NOT NULL,
                                   ask_price double precision,
                                   bid_price double precision,
                                   high_price double precision,
                                   last_price double precision,
                                   low_price double precision,
                                   symbol_name character varying(255)
);


ALTER TABLE public.instrument OWNER TO dbmanager;

--
-- Name: instrument_id_seq; Type: SEQUENCE; Schema: public; Owner: dbmanager
--

CREATE SEQUENCE public.instrument_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.instrument_id_seq OWNER TO dbmanager;

--
-- Name: instrument_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbmanager
--

ALTER SEQUENCE public.instrument_id_seq OWNED BY public.instrument.id;


--
-- Name: quote_bin_five_minute; Type: TABLE; Schema: public; Owner: dbmanager
--

CREATE TABLE public.quote_bin_five_minute (
                                              id bigint NOT NULL,
                                              created_at timestamp without time zone NOT NULL,
                                              updated_at timestamp without time zone NOT NULL,
                                              ask_price double precision,
                                              bid_price double precision,
                                              symbol_name character varying(255),
                                              "timestamp" timestamp without time zone
);


ALTER TABLE public.quote_bin_five_minute OWNER TO dbmanager;

--
-- Name: quote_bin_five_minute_id_seq; Type: SEQUENCE; Schema: public; Owner: dbmanager
--

CREATE SEQUENCE public.quote_bin_five_minute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.quote_bin_five_minute_id_seq OWNER TO dbmanager;

--
-- Name: quote_bin_five_minute_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbmanager
--

ALTER SEQUENCE public.quote_bin_five_minute_id_seq OWNED BY public.quote_bin_five_minute.id;


--
-- Name: quote_bin_one_minute; Type: TABLE; Schema: public; Owner: dbmanager
--

CREATE TABLE public.quote_bin_one_minute (
                                             id bigint NOT NULL,
                                             created_at timestamp without time zone NOT NULL,
                                             updated_at timestamp without time zone NOT NULL,
                                             ask_price double precision,
                                             bid_price double precision,
                                             symbol_name character varying(255),
                                             "timestamp" timestamp without time zone
);


ALTER TABLE public.quote_bin_one_minute OWNER TO dbmanager;

--
-- Name: quote_bin_one_minute_id_seq; Type: SEQUENCE; Schema: public; Owner: dbmanager
--

CREATE SEQUENCE public.quote_bin_one_minute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.quote_bin_one_minute_id_seq OWNER TO dbmanager;

--
-- Name: quote_bin_one_minute_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbmanager
--

ALTER SEQUENCE public.quote_bin_one_minute_id_seq OWNED BY public.quote_bin_one_minute.id;

--
-- Name: instrument id; Type: DEFAULT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.instrument ALTER COLUMN id SET DEFAULT nextval('public.instrument_id_seq'::regclass);


--
-- Name: quote_bin_five_minute id; Type: DEFAULT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.quote_bin_five_minute ALTER COLUMN id SET DEFAULT nextval('public.quote_bin_five_minute_id_seq'::regclass);


--
-- Name: quote_bin_one_minute id; Type: DEFAULT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.quote_bin_one_minute ALTER COLUMN id SET DEFAULT nextval('public.quote_bin_one_minute_id_seq'::regclass);


--
-- Name: instrument instrument_pkey; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.instrument
    ADD CONSTRAINT instrument_pkey PRIMARY KEY (id);


--
-- Name: quote_bin_five_minute quote_bin_five_minute_pkey; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.quote_bin_five_minute
    ADD CONSTRAINT quote_bin_five_minute_pkey PRIMARY KEY (id);


--
-- Name: quote_bin_one_minute quote_bin_one_minute_pkey; Type: CONSTRAINT; Schema: public; Owner: dbmanager
--

ALTER TABLE ONLY public.quote_bin_one_minute
    ADD CONSTRAINT quote_bin_one_minute_pkey PRIMARY KEY (id);