update tags set taggroup_id = (select id from taggroups where name = 'PLATFORM')
where name in (
    'native',
    'amd64',
    '32bit',
    'Browser',
    'Wine',
    'eON',
    'DOSBox',
    'Crossover',
    'Crossover Games',
    'DXVK',
    'Proton',
    'ScummVM',
    'Wine-Port',
    'Pointrelease',
    'Playonlinux',
    'DOS',
    'MS-DOS',
    'DOSBOX',
    'Linux/PPC',
    'C64',
    'Raspberry Pi',
    'Android',
    'snes',
    'Alpha',
    'Nintendo',
    'Amiga'
);