import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

pd.set_option('display.max_columns', None)
pd.set_option('display.width', 120)

ratings = pd.read_csv("~/Downloads/ml-latest-small/ratings.csv")
movies = pd.read_csv("~/Downloads/ml-latest-small/movies.csv")

data = pd.merge(ratings, movies, on ="movieId")
#print(data.head(10))

ratings_average = pd.DataFrame(data.groupby("title")["rating"].mean())
ratings_average["ratings_count"] = pd.DataFrame(data.groupby("title")["rating"].count())
#print(ratings_average.head(10))

ratings_matrix = data.pivot_table(index="userId",columns="title",values="rating")
#print(ratings_matrix.head(10))

favourite_movie_ratings = ratings_matrix["Interstellar (2014)"]
#print(favourite_movie_ratings.head(10))

similar_movies = ratings_matrix.corrwith(favourite_movie_ratings)
#print(similar_movies.head(10))

correlation = pd.DataFrame(similar_movies, columns = ["Correlation"])
correlation.dropna(inplace=True)
correlation = correlation.join(ratings_average["ratings_count"])
#print(correlation.sort_values("Correlation",ascending=False).head(10))

recommendation = correlation[correlation["ratings_count"]>100].sort_values("Correlation",ascending=False)
recommendation = recommendation.merge(movies, on = "title")
print(recommendation.head(10))
