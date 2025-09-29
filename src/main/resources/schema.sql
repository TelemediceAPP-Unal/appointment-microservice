-- Example schema hints for Postgres (Hibernate update handles defaults)
-- create table appointments (...);
-- create table outbox (...);

-- Table: appointments
CREATE TABLE IF NOT EXISTS appointments (
	id VARCHAR(36) PRIMARY KEY,
	patient_id VARCHAR(255) NOT NULL,
	practitioner_id VARCHAR(255) NOT NULL,
	start_at TIMESTAMP WITH TIME ZONE NOT NULL,
	end_at TIMESTAMP WITH TIME ZONE,
	state VARCHAR(50) NOT NULL,
	version INTEGER NOT NULL
);

-- Table: outbox
CREATE TABLE IF NOT EXISTS outbox (
	id VARCHAR(36) PRIMARY KEY,
	aggregate_id VARCHAR(255) NOT NULL,
	type VARCHAR(255) NOT NULL,
	payload_json TEXT NOT NULL,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL,
	published BOOLEAN NOT NULL DEFAULT FALSE
);

