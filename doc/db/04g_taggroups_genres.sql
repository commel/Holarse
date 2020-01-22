update tags set taggroup_id = (select id from taggroups where name = 'GENRE')
where name in (
    'Arcade',
    'Shooter',
    'Ego-Shooter',
    'Action',
    'Echtzeitstrategie',
    'Adventure',
    'Horror',
    'RTS',
    'Strategie',
    'Weltraum',
    'Wirtschaftssimulation',
    'Rundenstrategie',
    'MMORPG',
    'Rollenspiele',
    'Zombie',
    'Flugsimulator',
    'Puzzle',
    'Städtebau',
    'Simulation',
    'Tower Defense',
    'FPS',
    'Landwirtschaft', 
    'Wikinger',
    'Musik',
    'Fußball',
    'Open World',
    'Post-Apokalypse',
    'Point and Click',
    'City Builder',
    'Action-Adventure',
    'Aufbaustrategie',
    'Taktik',
    'Sci-Fi',
    'Third Person Shooter',
    'Pinball',
    'Flipper',
    'Fantasy',
    'Rogue-like',
    'Run and gun',
    'Lernen',
    'Oldschool',
    'jump''n''run',
    'platformer',
    'Casual',
    'Voxel',
    'Visual Novel',
    'rundenbasiert',
    'Shoot''em Up',
    'Sandbox',
    '4X-Strategie',
    'Weltraumsimulation',
    'Survival',
    'Golf',
    'Minigolf',
    'crafting',
    'Detektivspiel',
    'Piraten',
    'manga',
    'MMORTS',
    'Krieg',
    'Brettspiele',
    'Kampf',
    '2D Side-Scrolling',
    'Grand Strategy',
    'Mittelalter',
    'dungeon crawler',
    'Kartenspiel',
    'Musikspiel',
    'Retro',
    'JRPG',
    'Abenteuer',
    'Management',
    'MOBA',
    'Gelegenheitsspiel',
    'Politiksimulation',
    'Gesellschaftsspiel',
    'Artillery',
    'Metroidvania',
    'Geschicklichkeit',
    'Hack''n''Slash',
    'Beat''em''up',
    'Stealth',
    'Einbruch',
    'Verkehrssimulation',
    'Hacker',
    'Anime',
    'Sport',
    'Transport',
    'Schiffssimulation',
    'Schiffe',
    'Dating Sim',
    'Top-Down',
    'Burgenbau',
    'Unterwasser',
    'Strategie',
    'MMOFPS',
    'BridgeSim',
    'Rail-Shooter',
    'Fighting',
    'Logik',
    'Karten',
    'Interactive Book',
    '2. Weltkrieg',
    '1. Weltkrieg',
    'rhythmus',
    'rhythm',
    'Walking Simulator',
    'Krimi',
    'Hard Sci-Fi',
    'Splatter',
    'dungeon',
    'Baustelle',
    'Dinosaurier',
    'TV',
    'looten und leveln',
    'Medizin',
    'Coding',
    'Kolonisierung',
    'Ausmalen',
    'D&D',
    'Third Person',
    'Interactive Tale', 
    'Western',
    'Permadeath',
    'Blöcke',
    'Rätsel',
    'Tycoon',
    'infinite runner',
    'Baseball',
    'Space Combat',
    'Wimmelbild',
    'Winter',
    'Steampunk',
    'Sportsimulation',
    'Racing',
    'Warhammer', 
    'Noir',
    'Mythologie',
    'Godlike',
    'God',
    'Brückenbau',
    'Dystopia',
    'Rally',
    'Gore',
    'Soccer',
    'Cards',
    'Helikopter',
    'Militär',
    'Hubschrauber',
    'Violent',
    'Motocross',
    'Motorrad',
    'Football',
    'Kochen',
    'Erotik',
    'Table-Top',
    'Ameisen',
    'Kindgerecht',
    'Relaxing',
    'Würfelspiel',
    'Feuerwehr',
    'Bildung',
    'Cyberpunk',
    'Roboter',
    'Robots',
    'Battle Royale',
    'U-Boot',
    'rogue-lite',
    'Party',
    'run''n''gun',
    'Surreal',
    'Strategy',
    'Schleichen',
    'Escape',
    'Witzig',
    'Wilder Westen',
    'Mystery',
    'Gewalt',
    'Off-Road',
    'Suchspiel',
    'Angeln',
    'Fischen',
    'Drama',
    'Samurai',
    'Raumstation',
    'Vietnam',
    'Krankenhaus',
    'Zeitreisen', 
    'Züge',
    'First Person',
	'historisch', 
	'Rennspiele',
	'Sportspiele',
	'Weltraumshooter',
	'Kinder',
	'Fussball Manager',
	'Skating',
	'Social',
	'infinte runner',
	'boxen',
	'isometrisch',
	'Exploration',
	'Rom',
	'Schwer',
	'generierter Inhalt',
	'Wuselfaktor',
	'text-basiert',
	'Mech', 
	'Meditation',
	'MMO',
	'Procedural Generated',
	'Programmierung',
	'Psycho',
	'RPG',
	'Stategie',
	'terminal',
	'Pixelart',
	'pixelgrafik'
);