
UPDATE ACT_FO_DATABASECHANGELOGLOCK SET LOCKED = 1, LOCKEDBY = '192.168.1.5 (192.168.1.5)', LOCKGRANTED = TIMESTAMP('2019-03-13 21:57:50.258') WHERE ID = 1 AND LOCKED = 0;

ALTER TABLE ACT_FO_FORM_INSTANCE ADD SCOPE_ID_ VARCHAR(255);

ALTER TABLE ACT_FO_FORM_INSTANCE ADD SCOPE_TYPE_ VARCHAR(255);

ALTER TABLE ACT_FO_FORM_INSTANCE ADD SCOPE_DEFINITION_ID_ VARCHAR(255);

CALL SYSPROC.ADMIN_CMD ('REORG TABLE ACT_FO_FORM_INSTANCE');

INSERT INTO ACT_FO_DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('2', 'flowable', 'org/flowable/form/db/liquibase/flowable-form-db-changelog.xml', CURRENT TIMESTAMP, 2, '7:4850f9311e7503d7ea30a372e79b4ea2', 'addColumn tableName=ACT_FO_FORM_INSTANCE', '', 'EXECUTED', NULL, NULL, '3.5.3', '2510670391');

UPDATE ACT_FO_DATABASECHANGELOGLOCK SET LOCKED = 0, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

