Assignment 1 for Cloud Computing

Student: Binfeng Yuan 	(by2209)
	 Qi Yang	(qy2152)

App Url: http://tweetmap-env.elasticbeanstalk.com/index.jsp

GitHub Url: https://github.com/vannirobben/TweetStream

Description and highlights:
Our project runs on AWS beanstalk server with load balancing. Users can select keywords and click on the “”show on Map” button. The red spots represents the location of the person who sent the twit. You can also click on the stop button and all red spots will be removed. The Google map is automatically renewed every 5 seconds. If the red spots are twinkled, it means the map is refreshing automatically.

There is a backend program running continuously to get new twits from Twitter API. The database is running with MySQL on AWS RDS. We set the largest number of twits in our database to be 1000000. If a new twit comes in when the database is fully loaded, we will delete the oldest twit from our database.

Note: For the source code, we did not upload any credential files or any passwords for logging into our database or server.