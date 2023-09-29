delete from hits;

ALTER TABLE hits ALTER COLUMN id RESTART WITH 1;