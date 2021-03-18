-- recommendations
CREATE DATABASE recommendations;
\c recommendations;

CREATE TABLE t_recommendations(
    id VARCHAR(36),
    data JSONB,
    PRIMARY KEY (id)
);

-- catalog
CREATE DATABASE catalog;
\c catalog;

CREATE TABLE t_movie(
    id VARCHAR(36),
    data JSONB,
    PRIMARY KEY (id)
);

INSERT INTO
    t_movie (id, data)
VALUES
    (
        '2cfcdc2a-3fe4-4511-bd73-0234bd8aca15',
        '{
            "id": "2cfcdc2a-3fe4-4511-bd73-0234bd8aca15",
            "title": "Paranormal Activity",
            "tags": ["horror", "mistery"],
            "release_year": 2007
        }' :: jsonb
    ),
    (
        '9717da82-1fd1-44d3-898c-2524a360a827',
        '{
            "id": "9717da82-1fd1-44d3-898c-2524a360a827",
            "title": "Get Out",
            "tags": ["horror", "mistery", "thriller"],
            "release_year": 2017
        }' :: jsonb
    ),
    (
        'dbf20b72-30d3-4aa2-ad3e-3cc633520032',
        '{
            "id": "dbf20b72-30d3-4aa2-ad3e-3cc633520032",
            "title": "Kimi no na wa",
            "tags": ["animation", "drama", "fantasy", "romance"],
            "release_year": 2016
        }' :: jsonb
    ),
    (
        '55598532-dc16-4ad4-b497-965bc8ad9c02',
        '{
            "id": "55598532-dc16-4ad4-b497-965bc8ad9c02",
            "title": "Pulp Fiction",
            "tags": ["thriller", "drama", "classic"],
            "release_year": 1994
        }' :: jsonb
    ),
    (
        'a299eb56-8cc7-4b32-8952-03976e4357eb',
        '{
            "id": "a299eb56-8cc7-4b32-8952-03976e4357eb",
            "title": "The thing",
            "tags": ["horror", "mystery", "sci-Fi", "classic"],
            "release_year": 1982
        }' :: jsonb
    ),
    (
        'd14fb662-5f0a-46d0-8d72-7ce3c629aeba',
        '{
            "id": "d14fb662-5f0a-46d0-8d72-7ce3c629aeba",
            "title": "The Artist 2011",
            "tags": ["romance", "comedy", "drama"],
            "release_year": 2011
        }' :: jsonb
    ),
    (
        '5c4bf3a5-1806-4afd-8602-ec3611250d7a',
        '{
            "id": "5c4bf3a5-1806-4afd-8602-ec3611250d7a",
            "title": "A Star Is Born",
            "tags": ["drama", "music", "romance"],
            "release_year": 2018
        }' :: jsonb
    ),
    (
        '7805750a-ecf3-4be9-9c49-8770b62008ae',
        '{
            "id": "7805750a-ecf3-4be9-9c49-8770b62008ae",
            "title": "Incredibles 2",
            "tags": ["animation", "action", "adventure"],
            "release_year": 2018
        }' :: jsonb
    ),
    (
        '0e443ab8-828f-40c6-adb3-8cefe6df651e',
        '{
            "id": "0e443ab8-828f-40c6-adb3-8cefe6df651e",
            "title": "The Last Samurai",
            "tags": ["action", "adventure", "drama", "history", "war"],
            "release_year": 2003
        }' :: jsonb
    ),
    (
        '85034615-d30a-49fa-a8a7-5db81097123a',
        '{
            "id": "85034615-d30a-49fa-a8a7-5db81097123a",
            "title": "Furious 6",
            "tags": ["action", "crime", "thriller"],
            "release_year": 2013
        }' :: jsonb
    ),
    (
        'efdd3d2b-ea9a-43de-839f-bf0956e45903',
        '{
            "id": "efdd3d2b-ea9a-43de-839f-bf0956e45903",
            "title": "Avengers: Endgame",
            "tags": ["action", "adventure", "sci-Fi"],
            "release_year": 2019
        }' :: jsonb
    ),
    (
        '4c4369df-2f45-4023-8f8d-a339fd0c6869',
        '{
            "id": "4c4369df-2f45-4023-8f8d-a339fd0c6869",
            "title": "The Godfather",
            "tags": ["crime", "drama", "thriller", "classic"],
            "release_year": 1972
        }' :: jsonb
    ),
    (
        '3aa5c263-2774-4d7e-a36a-29c04f36cbfd',
        '{
            "id": "3aa5c263-2774-4d7e-a36a-29c04f36cbfd",
            "title": "Red River",
            "tags": ["Western", "Action", "Adventure"],
            "release_year": 1948
        }' :: jsonb
    ),
    (
        '51b032b0-f2dd-4a8d-995a-f140751d7fac',
        '{
            "id": "51b032b0-f2dd-4a8d-995a-f140751d7fac",
            "title": "Toy Story 4",
            "tags": ["animation", "adventure", "comedy", "family", "fantasy"],
            "release_year": 2019
        }' :: jsonb
    ),
    (
        '2e1045a7-1c3f-49d9-9374-b5559a7d9a92',
        '{
            "id": "2e1045a7-1c3f-49d9-9374-b5559a7d9a92",
            "title": "Harry Potter and the Deathly Hallows: Part 1",
            "tags": ["adventure", "family", "fantasy"],
            "release_year": 2010
        }' :: jsonb
    ),
    (
        'b6dbb0f7-ce1e-43af-9aff-e114339a2bee',
        '{
            "id": "b6dbb0f7-ce1e-43af-9aff-e114339a2bee",
            "title": "Enter the Dragon",
            "tags": ["action", "crime", "drama", "thriller"],
            "release_year": 1973
        }' :: jsonb
    );