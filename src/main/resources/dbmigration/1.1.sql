-- apply changes
alter table patient alter column blood_type varchar(4);
alter table patient alter column blood_type set null;
