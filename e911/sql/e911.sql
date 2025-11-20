drop table if exists e911;
create table e911
(voip_dns		varchar( 255 ),
voip_ip_address		varchar( 25 ),
voip_mac		varchar( 25 ),
location_count		int,
auth_count		int,
current_check_in	datetime,
current_nas_id		varchar( 25 ),
current_nas_port	varchar( 10 ),
previous_check_in	datetime,
previous_nas_id		varchar( 25 ),
previous_nas_ip		varchar( 25 ),
previous_nas_port	varchar( 10 ));

create index ie91101 on e911 ( voip_mac );
