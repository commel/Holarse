update tags set taggroup_id = (select id from taggroups where name = 'MULTIPLAYER')
where name in (
    '10+Spieler',
    '10-Spieler',
    '10-Spieler',
'1-255 Spieler LAN',
'1-255 Spieler Online',
'12-Spieler',
'15-Spieler vs online',
'16-Spieler',
'16-Spieler co-op online',
'200-Spieler',
'20-Spieler',

'2-Spieler',
'2-Spieler lan',
'2-Spieler Lokal',
'32-Spieler',
'3-Spieler',
'3-Spieler Lokal',
'4-Spieler',
'4-spieler lan',
'4-Spieler Lokal',
'4-Spieler Online',

'5-Spieler',
'5-Spieler Lokal',
'64-Spieler',

'6-Spieler',
'6-spieler-lan',
'7-Spieler',
'8-Spieler',
'8-spieler-lan',

'bis zu 12 Spieler',
'bis zu 20 Spieler',
'bis zu 2 Spieler',
'bis zu 3 Spieler',
'bis zu 4 Spieler',
'bis zu 8 Spieler',
'Coop',
'Co-Op',
'Crossplattform',
'Plattformspezifisch',
'dedicated server',
'Einzelspieler',
'gut spielbar zu 10',
'gut spielbar zu 11',
'gut spielbar zu 12',
'gut spielbar zu 13',
'gut spielbar zu 14',
'gut spielbar zu 15',
'gut spielbar zu 16',
'gut spielbar zu 17',
'gut spielbar zu 18',
'gut spielbar zu 19',
'gut spielbar zu 2',
'gut spielbar zu 20',
'gut spielbar zu 3',
'gut spielbar zu 4',
'gut spielbar zu 5',
'gut spielbar zu 6',
'gut spielbar zu 7',
'gut spielbar zu 8',
'gut spielbar zu 9',
'kein LAN',
'keinLAN',
'LAN',
'mehrspieler',
'Multiplayer',
'Multiplayer Lokal',
'Netzwerk',
'online',
'Online Game',
'splitscreen',
'split-screen'
);