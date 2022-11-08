CREATE TABLE IF NOT EXISTS node_pagevisits (
    id bigserial, -- eigner index, shortcut via serial, darf wegen partition kein primary key sein
    nodeid integer,
    visitorid varchar(255),
    campaign_keyword varchar(255),
    campaign_name varchar(255),
    httpstatus integer,
    ipaddress varchar(255),
    referer varchar(255),
    searchword varchar(255),
    url varchar(2083),
    useragent varchar(255),
    bot boolean DEFAULT false,
    accessed timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL
) PARTITION BY range(accessed);

---- Index for accesslog
CREATE INDEX IF NOT EXISTS node_pagevisits_idx ON node_pagevisits (accessed);
CREATE INDEX IF NOT EXISTS node_pagevisits_nodeid_idx ON node_pagevisits (nodeid, accessed);

---- Table data for logging (FIXME!)
CREATE TABLE node_pagevisits_2019 PARTITION OF node_pagevisits FOR VALUES FROM ('2019-01-01') TO ('2020-01-01');
CREATE TABLE node_pagevisits_2020 PARTITION OF node_pagevisits FOR VALUES FROM ('2020-01-01') TO ('2021-01-01');
CREATE TABLE node_pagevisits_2021 PARTITION OF node_pagevisits FOR VALUES FROM ('2021-01-01') TO ('2022-01-01');
CREATE TABLE node_pagevisits_2022 PARTITION OF node_pagevisits FOR VALUES FROM ('2022-01-01') TO ('2023-01-01');
CREATE TABLE node_pagevisits_2023 PARTITION OF node_pagevisits FOR VALUES FROM ('2023-01-01') TO ('2024-01-01');
CREATE TABLE node_pagevisits_2024 PARTITION OF node_pagevisits FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');
CREATE TABLE node_pagevisits_2025 PARTITION OF node_pagevisits FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');
