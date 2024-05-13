__header__
RequestId: //<request id>//
Version: //<api version>//
AcquirerId: //<acquirer id>//
Channel: //<channel>//
MerchantId: //<merchant id>//
TerminalId: //<terminal id>//
Authorization: Bearer //<access token>//

__body__
{
	"paymentMethod": "//<payment method>//",
	"notices": [
		{
			"amount": //<amount>//,
			"paTaxCode": "//<pa tax code>//",
			"transfers": [
				{
					"paTaxCode": "//<pa tax code #1>//",
					"category": "//<category #1>//"
				},
				.
				.
				.
			]
		},
		.
		.
		.
	]
}