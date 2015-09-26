round robin mission \n					
\n
main activity:\n
1) listview \n
	for now there are 5 items. [105-facebook,11-apple,10-google,162-alibaba,44-twitter] \n
	every item contain icon name and rating.\n
	in any giving moment the user can see only three items at a time\n
2) storage\n
	the application storage the company name and icon in preference storage.\n
	The application insert the data only when the program is installed.\n
	The application storage the data in the next fashion: <company-id,company-name>, <company-name, company-icon>\n
3) communication\n																										
	every second the application send a http post request.\n																	
	the body of the request contain array with all the company id number. \n 												
4) update\n
	update the rating base on the info we get from http request.\n
	if the rating is going down please update\n
	
	

	
	
	
