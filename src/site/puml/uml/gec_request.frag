__header__
Ocp-Apim-Subscription-Key: //<gec api-key>//

__body__
{
	"paymentAmount": //<amount>//,
	"primaryCreditorInstitution": "//<pa tax code>//",
	"paymentMethod": "//<remapped payment method>//",
	"touchpoint": "//<remapped channel>//",
	"idPspList": [
		{
			"idPsp": "//<psp id>//",
			"idBrokerPsp": "//<psp broker id>//",
			"idChannel": "//<channel id>//"
		}
	],
	"transferList": [
		{
			"creditorInstitution": "//<pa tax code>//",
			"transferCategory": "//<category #1>//"
		},
		.
		.
		.
	]
}