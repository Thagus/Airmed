-- apply changes
alter table vital_sign alter column hemoglobin decimal(5,2);
alter table vital_sign alter column hemoglobin drop default;
alter table vital_sign alter column hemoglobin set null;
