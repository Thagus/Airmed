-- apply changes
alter table vital_sign alter column pressure_d set default 0;
alter table vital_sign alter column pressure_s set default 0;
alter table vital_sign alter column pulse set default 0;
alter table vital_sign alter column temperature set default 0;
alter table vital_sign alter column breath set default 0;
alter table vital_sign add column glucose integer default 0 not null;
alter table vital_sign add column hemoglobin integer default 0 not null;
alter table vital_sign add column cholesterol integer default 0 not null;
alter table vital_sign add column triglycerides integer default 0 not null;

