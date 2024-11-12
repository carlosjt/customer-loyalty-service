CREATE USER cls_app WITH PASSWORD 'CLS@123!';
GRANT ALL PRIVILEGES ON DATABASE customer_loyalty to cls_app;
\connect customer_loyalty
CREATE SCHEMA customer_loyalty_core;
GRANT ALL PRIVILEGES ON SCHEMA customer_loyalty_core to cls_app;
ALTER DATABASE customer_loyalty SET search_path TO customer_loyalty_core;
DROP SCHEMA public CASCADE;
