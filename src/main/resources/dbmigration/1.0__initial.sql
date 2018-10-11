-- apply changes
create table appointment (
  id                            integer auto_increment not null,
  patient_id                    integer not null,
  date_time                     timestamp not null,
  answered                      boolean default false not null,
  constraint pk_appointment primary key (id)
);

create table consultation (
  id                            integer auto_increment not null,
  patient_id                    integer not null,
  date_time                     timestamp not null,
  diagnostic                    varchar(255),
  prognosis                     varchar(255),
  prescription_id               integer not null,
  constraint uq_consultation_prescription_id unique (prescription_id),
  constraint pk_consultation primary key (id)
);

create table dose (
  id                            integer auto_increment not null,
  dose                          varchar(255) not null,
  medicine_id                   integer,
  constraint pk_dose primary key (id)
);

create table exploration (
  id                            integer auto_increment not null,
  consultation_id               integer,
  awareness                     varchar(255),
  collaboration                 varchar(255),
  mobility                      varchar(255),
  attitude                      varchar(255),
  nutrition                     varchar(255),
  hydration                     varchar(255),
  constraint uq_exploration_consultation_id unique (consultation_id),
  constraint pk_exploration primary key (id)
);

create table measurement (
  id                            integer auto_increment not null,
  consultation_id               integer,
  weight                        integer not null,
  height                        integer not null,
  constraint uq_measurement_consultation_id unique (consultation_id),
  constraint pk_measurement primary key (id)
);

create table medicine (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  constraint uq_medicine_name unique (name),
  constraint pk_medicine primary key (id)
);

create table patient (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  lastname                      varchar(255) not null,
  gender                        varchar(255) not null,
  birthdate                     date not null,
  blood_type                    varchar(3) not null,
  email                         varchar(255),
  phone                         varchar(255),
  cellphone                     varchar(255),
  constraint pk_patient primary key (id)
);

create table prescription (
  id                            integer auto_increment not null,
  patient_id                    integer not null,
  date_time                     timestamp not null,
  notes                         varchar(255),
  constraint pk_prescription primary key (id)
);

create table prescription_treatment (
  prescription_id               integer not null,
  treatment_id                  integer not null,
  constraint pk_prescription_treatment primary key (prescription_id,treatment_id)
);

create table prescription_study (
  prescription_id               integer not null,
  study_id                      integer not null,
  constraint pk_prescription_study primary key (prescription_id,study_id)
);

create table prescription_dose (
  prescription_id               integer not null,
  dose_id                       integer not null,
  constraint pk_prescription_dose primary key (prescription_id,dose_id)
);

create table record (
  id                            integer auto_increment not null,
  patient_id                    integer not null,
  family_bg                     clob,
  personal_bg                   clob,
  systems_bg                    clob,
  allergies                     clob,
  notes                         clob,
  constraint uq_record_patient_id unique (patient_id),
  constraint pk_record primary key (id)
);

create table setting (
  setting                       varchar(255) not null,
  value                         varchar(255) not null,
  constraint pk_setting primary key (setting)
);

create table study (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  description                   varchar(255),
  constraint pk_study primary key (id)
);

create table study_result (
  id                            integer auto_increment not null,
  study_id                      integer not null,
  record_id                     integer not null,
  date                          date not null,
  result                        clob not null,
  constraint pk_study_result primary key (id)
);

create table surgery (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  record_id                     integer not null,
  date                          date,
  description                   varchar(255),
  constraint pk_surgery primary key (id)
);

create table treatment (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  description                   varchar(255),
  constraint pk_treatment primary key (id)
);

create table treatment_dose (
  treatment_id                  integer not null,
  dose_id                       integer not null,
  constraint pk_treatment_dose primary key (treatment_id,dose_id)
);

create table vital_sign (
  id                            integer auto_increment not null,
  consultation_id               integer,
  pressure_d                    integer not null,
  pressure_s                    integer not null,
  pulse                         integer not null,
  temperature                   integer not null,
  breath                        integer not null,
  constraint uq_vital_sign_consultation_id unique (consultation_id),
  constraint pk_vital_sign primary key (id)
);

create index ix_appointment_patient_id on appointment (patient_id);
alter table appointment add constraint fk_appointment_patient_id foreign key (patient_id) references patient (id) on delete restrict on update restrict;

create index ix_consultation_patient_id on consultation (patient_id);
alter table consultation add constraint fk_consultation_patient_id foreign key (patient_id) references patient (id) on delete restrict on update restrict;

alter table consultation add constraint fk_consultation_prescription_id foreign key (prescription_id) references prescription (id) on delete restrict on update restrict;

create index ix_dose_medicine_id on dose (medicine_id);
alter table dose add constraint fk_dose_medicine_id foreign key (medicine_id) references medicine (id) on delete restrict on update restrict;

alter table exploration add constraint fk_exploration_consultation_id foreign key (consultation_id) references consultation (id) on delete restrict on update restrict;

alter table measurement add constraint fk_measurement_consultation_id foreign key (consultation_id) references consultation (id) on delete restrict on update restrict;

create index ix_prescription_patient_id on prescription (patient_id);
alter table prescription add constraint fk_prescription_patient_id foreign key (patient_id) references patient (id) on delete restrict on update restrict;

create index ix_prescription_treatment_prescription on prescription_treatment (prescription_id);
alter table prescription_treatment add constraint fk_prescription_treatment_prescription foreign key (prescription_id) references prescription (id) on delete restrict on update restrict;

create index ix_prescription_treatment_treatment on prescription_treatment (treatment_id);
alter table prescription_treatment add constraint fk_prescription_treatment_treatment foreign key (treatment_id) references treatment (id) on delete restrict on update restrict;

create index ix_prescription_study_prescription on prescription_study (prescription_id);
alter table prescription_study add constraint fk_prescription_study_prescription foreign key (prescription_id) references prescription (id) on delete restrict on update restrict;

create index ix_prescription_study_study on prescription_study (study_id);
alter table prescription_study add constraint fk_prescription_study_study foreign key (study_id) references study (id) on delete restrict on update restrict;

create index ix_prescription_dose_prescription on prescription_dose (prescription_id);
alter table prescription_dose add constraint fk_prescription_dose_prescription foreign key (prescription_id) references prescription (id) on delete restrict on update restrict;

create index ix_prescription_dose_dose on prescription_dose (dose_id);
alter table prescription_dose add constraint fk_prescription_dose_dose foreign key (dose_id) references dose (id) on delete restrict on update restrict;

alter table record add constraint fk_record_patient_id foreign key (patient_id) references patient (id) on delete restrict on update restrict;

create index ix_study_result_study_id on study_result (study_id);
alter table study_result add constraint fk_study_result_study_id foreign key (study_id) references study (id) on delete restrict on update restrict;

create index ix_study_result_record_id on study_result (record_id);
alter table study_result add constraint fk_study_result_record_id foreign key (record_id) references record (id) on delete restrict on update restrict;

create index ix_surgery_record_id on surgery (record_id);
alter table surgery add constraint fk_surgery_record_id foreign key (record_id) references record (id) on delete restrict on update restrict;

create index ix_treatment_dose_treatment on treatment_dose (treatment_id);
alter table treatment_dose add constraint fk_treatment_dose_treatment foreign key (treatment_id) references treatment (id) on delete restrict on update restrict;

create index ix_treatment_dose_dose on treatment_dose (dose_id);
alter table treatment_dose add constraint fk_treatment_dose_dose foreign key (dose_id) references dose (id) on delete restrict on update restrict;

alter table vital_sign add constraint fk_vital_sign_consultation_id foreign key (consultation_id) references consultation (id) on delete restrict on update restrict;

