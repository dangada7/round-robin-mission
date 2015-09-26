round robin mission

main activity: 
1) listview 
	for now there are 5 items. [105-facebook,11-apple,10-google,162-alibaba,44-twitter]
	every item contain icon name and rating.
	in any giving moment the user can see only three items at a time
2) storage
	the application storage the company name and icon in preference storage.
	The application insert the data only when the program is installed.
	The application storage the data in the next fashion: <company-id,company-name>, <company-name, company-icon>
3) communication																										
	every second the application send a http post request.																	
	the body of the request contain array with all the company id number.  												
4) update
	update the rating base on the info we get from http request.
	if the rating is going down please update
	
	

	
	
	
