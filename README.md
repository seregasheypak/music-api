# music-api
Simple Rest API to get information about an specific music artist.

The API takes a ​MBID (MusicBrainz Identifier)​ and return a JSON containing the following:
 * A list of all albums the artist has released and links to its corresponding album cover art.
 * A description of the artist fetched from Genius.com

## Tech specs
* Java
* Maven 3
* SpringBoot Framework

## How to build it and run it?
In order to run this api you just need to execute the following Maven command:

```
mvn spring-boot:run
```
This will use the application's default properties file (application.yml) and will start the RestAPI on http://localhost:8081

A properties file for production environment is provided (application-prod.yml. This overwrites the server to listen to port 8080 instead of 8081. Just especify "prod" profile when running the application:

```
mvn spring-boot: run -Dspring-boot.run.profiles=prod
```

## Development Tools
  * IntelliJ IDEA
  * Spring Framework: very popular and well supported framework for building Java based applications. Provides a huge amount of out of the box solutions for common tasks.

## Stuff left out/shortcuts
  * Service security (authentication/authorization) was left out, it was out of scope for version 1. JWT authentication + Spring Security its an option for furute versions
  * Cache strategy: no cache strategy was implemented, given the nature of the information requested and the trafict expected a cache strategy will avoid hitting the 3rd party services more than needed, as well as improve the response time
  * API documentation: a nice and easy to use way to let the API consumers know how to use the API.

## How and why did you select the source of the profile information?

After taking a look at the sources provided by Music Brainz I choose to use Genius as the source to get artist profile information. Other sources have out of date developer API (BBC used to have one, now is deprecated), and other sources didn't provide a good documentation. Genius API is easy to use, stright forward and provides a JSON responses, which once again are easy to serialized into parse and work with.

## Sample requests:

Just make an HTTP get to:

- Coldplay: http://localhost:8081/api/v1/artist/cc197bad-dc9c-440d-a5b5-d52ba2e14234
- Pink Floyd: http://localhost:8081/api/v1/artist/83d91898-7763-47d7-b03b-b92132375c47
- Of Monsters and Men: http://localhost:8081/api/v1/artist/9e103f85-7af7-41d7-b83b-49ba8f0c5abf
- Malpais: http://localhost:8081/api/v1/artist/5a12467f-1164-4f36-ac0a-80270dab60df

## Sample respose:
```
{
    "mbid": "5a12467f-1164-4f36-ac0a-80270dab60df",
    "description": "The musicians of Malpaís perhaps best exemplify the “new sound” of Costa Rica – contemporary compositions that build on the solid tradition of their musical roots. The group takes its name from the most remote, jungle-cradled beach on the north-Pacific Nicoya Peninsula – the beach at road’s end. <a href=\"https://www.last.fm/music/Malpa%C3%ADs\">Read more on Last.fm</a>",
    "albums": [
        {
            "id": "0b639f0a-162e-3c7f-b8e6-ca846f0fcc67",
            "title": "Uno",
            "image": null
        },
        {
            "id": "cc3996d8-8476-4017-a37e-9597aaef9ff6",
            "title": "La canción de Adán",
            "image": "http://coverartarchive.org/release/e5f37b6f-c907-4c45-b667-4b2cf3a1058f/6946508683.jpg"
        },
        {
            "id": "d6004d04-8637-49b1-8409-b7909c6e312f",
            "title": "Hay Niños Aquí",
            "image": null
        }
    ]
}
```

