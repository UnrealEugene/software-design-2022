create table Event (
    id uuid not null primary key,
    creationTime timestamp not null,
    jsonData text not null
);

create index creationTime_idx on Event (creationTime);