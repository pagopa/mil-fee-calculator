# Data Dictionary

| Etichetta                            | Descrizione |
| ------------------------------------ | ----------- |
| `<abi>`                              | Codice ABI. |
| `<access token for storage account>` | Access token rilasciato a Microsoft Entra ID a mil-fee-calculator per accedere al blob storage. |
| `<access token>`                     | Access token inviato dal Client per adoperare l'API esposta da mil-fee-calculator. |
| `<acquirer id>`                      | ID dell'Acquirer cui fa capo il Client. |
| `<amount>`                           | Importo dell'avviso pagoPA. |
| `<api version>`                      | Versione dell'API richiesta dal Client. Al momento non è utilizzato. |
| `<bundle description>`               | Descrizione del pacchetto commissionale. |
| `<bundle id>`                        | ID del pacchetto commissionale. |
| `<bundle name>`                      | Nome del pacchetto commissionale. |
| `<category #1>`                      | Categoria dell'avviso pagoPA.
| `<channel id>`                       | ID del canale per GEC. |
| `<channel>`                          | Canale del Client (POS, ATM, ...). |
| `<ci bundle id>`                     | Codice fiscale dell'Ente Creditore che copre parte delle commissioni. |
| `<fee>`                              | Importo delle commissioni che deve pagare l'Utente. |
| `<gec api-key>`                      | API-key da adoperare per comunicare con GEC. |
| `<merchant id>`                      | ID del merchant cui fa capo il terminale (solo per il canale `POS`). |
| `<pa tax code #1>`                   | Codice fiscale dell'Ente Creditore. Il '#1' si riferisce alla possibilità futura di gestire il pagamemto in modalità *carrello*. |
| `<pa tax code>`                      | Codice fiscale dell'Ente Creditore. |
| `<payment method>`                   | Metodo di pagamento edoperato sul Client. |
| `<primary ci incurred fee>`          | Importo massimo che l'Ente Creditore è disposto a coprire relativamente alle commissioni da versare. |
| `<psp broker id>`                    | ID del broker del PSP che elabora il pagamento.
| `<psp id>`                           | ID del PSP che elabora il pagamaneto.
| `<psp name>`                         | Nome del PSP che elabora il pagamento.
| `<remapped channel>`                 | Canale adoperato espresso secondo la codifica usata da GEC. |
| `<remapped payment method>`          | Metodo di pagameto adoperato espresso secondo la codifica usata da GEC. | 
| `<remapped touch point>`             | Touchpoint adoperato espresso secondo la codifica usata da GEC. |
| `<request id>`                       | ID della richiesta inviata dal Client. |
| `<storage account>`                  | Nome dello storage account contenente i valori della tripla `<psp id>`, `<psp broker id>` e `<channel id>` corrispondente al `<acquirer id>`, da adoperare per interrogare GEC. |
| `<terminal id>`                      | ID del terminale (Client). |
