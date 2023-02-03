
db = connect( 'mongodb://localhost/mil' );
		
db.pspconf.insertMany([
	{
		_id: '6665626',
		pspConfiguration: { pspBroker: '6665626', pspId: 'AGID_01', pspPassword: '9808765' }
	}
])

printjson( db.pspconf.find( {} ) );
