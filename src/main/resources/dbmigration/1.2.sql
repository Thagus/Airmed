-- apply changes
alter table prescription alter column notes clob;
alter table study alter column description clob;
alter table surgery alter column description clob;
alter table treatment alter column description clob;
