create table TLG_SEND_GC_TB
(
    id bigserial not null primary key,
    tlg_text text,
    tlg_status smallint,
    tlg_webservice varchar,
    tlg_result varchar,
    gc_sas_id bigint,
    gc_sas_errorcode int,
    gc_sas_error_message varchar,
    row_state smallint,
    date_change timestamp
);

CREATE OR REPLACE FUNCTION notify_change_tlg_send_gc_tb() RETURNS TRIGGER AS $$
BEGIN
    if (NEW.tlg_status = 0) THEN
        PERFORM pg_notify('notify_channel_tlg_send_gc_tb', NEW.id::varchar);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


create trigger table_change
    AFTER INSERT OR UPDATE OR DELETE ON TLG_SEND_GC_TB
    FOR EACH ROW EXECUTE PROCEDURE notify_change_tlg_send_gc_tb();


CREATE TABLE GC_SEND_XML_TB
(
    ID bigserial not null primary key,
    GC_SERVICE VARCHAR NOT NULL,
    DATA_ID bigint NOT NULL,
    REQUEST_XML varchar,
    RESPONSE_XML varchar,
    STATUS int DEFAULT 0 NOT NULL,
    RESULT VARCHAR,
    GC_FUNCTION VARCHAR(20),
    ROW_STATE smallint DEFAULT 0 NOT NULL,
    date_change TIMESTAMP WITHOUT TIME ZONE DEFAULT LOCALTIMESTAMP NOT NULL
);