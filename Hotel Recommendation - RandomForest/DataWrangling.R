# Set working directory
setwd("C:/Users/saksh/Documents/4th Sem/Project/Expedia/Hotel Recommendation/")

#installing the required packages
install.packages("ggplot2")
install.packages("dplyr")
install.packages("caret")

# Load the libraries whic are required for the computation
library(data.table)
library(dplyr)
library(caret)
library(ggplot2)
library(GGally)

# Load the training and testing data
# so that we can perform the cleaning operation
train <- fread('train.csv', header=TRUE)
test <- fread('test.csv', header=TRUE)

# Training data too large - Split train to be more manageable for data analysis
set.seed(2016)
splitIndex <- createDataPartition(train$hotel_cluster, p = .02,
                                  list = FALSE,
                                  times = 1)

# splitIndex is for diving the data equally based on the number of clusters,
# since we have 100 clusters we don't want our data to miss any of them,
# therefore we use this function
expediaTrain <- train[splitIndex[, 1], ]

# Data Wrangling

# # Count number of NAs in all training data
na_count <- sapply(expediaTrain, function(y) sum(length(which(is.na(y)))))

# Observation suggest that only the orig_destination_distance column has NAs
View(as.data.frame(na_count))

# since orig_destination_distance is 7th column, we are going to compute the total
# percent of the data has NA's
# 36% of observations have NAs as distances
na_count[7] / length(expediaTrain$hotel_cluster)

# First let's generate a distance variable where we replace the NAs with the average distance
dist1 <- ifelse(is.na(expediaTrain$orig_destination_distance),
                mean(expediaTrain$orig_destination_distance, na.rm = TRUE),
                expediaTrain$orig_destination_distance)

#assiging orig_destination_distance to dist1, after replacing the NA's with the mean
expediaTrain$dist1 <- dist1

# Remove full train data to save computer memory
rm(train)

# Rename expediaTrain to train
train <- expediaTrain
rm(expediaTrain)

# Converting target dependent variable into a factor
train$hotel_cluster <- as.factor(train$hotel_cluster)
levels(train$hotel_cluster) <- make.names(levels(factor(train$hotel_cluster)))


# Feature engineering -----------------------------------------------------

# Generate month of holiday
train$checkInMonth <- as.Date(train$srch_ci)
train$checkInMonth <- month(train$checkInMonth)
train$checkInMonth[is.na(train$checkInMonth)] <- as.integer(names(sort(table(train$checkInMonth), decreasing=TRUE)[1]))

View(as.data.frame(train$checkInMonth))

# Generate measure to indicate popularity of hotel_cluster in each destination
hotel.popularity <- aggregate(train$is_booking, by = list(train$srch_destination_id, train$hotel_cluster), length)
temp <- aggregate(hotel.popularity$x, by = list(hotel.popularity$Group.1), sum)
hotel.popularity <- merge(hotel.popularity, temp, by = "Group.1")
rm(temp)
hotel.popularity$most.popular <- round(hotel.popularity$x.x / hotel.popularity$x.y, digits = 2)
hotel.popularity <- hotel.popularity[, c(1, 2, 3, 5)]
names(hotel.popularity) <- c("srch_destination_id", "hotel_cluster", "hotel.popularity.count", "hotel.popularity.prop")

# Generate tag to indicate top 5 hotel_clusters in each destination
temp <- hotel.popularity %>%
  group_by(srch_destination_id) %>%
  top_n(n = 5, wt = hotel.popularity.count)

temp$top5.dummy <- 1
temp <- temp[, c(1, 2, 5)]
hotel.popularity <- merge(hotel.popularity, temp, 
                          by = c("srch_destination_id", "hotel_cluster"),
                          all.x = TRUE)
hotel.popularity$top5.dummy[is.na(hotel.popularity$top5.dummy)] <- 0

train <- merge(train, hotel.popularity, by = c("srch_destination_id", "hotel_cluster"))

# Data analysis and visualisation -----------------------------------------

# Plotting the distribution of hotel clusters in training data
count <- table(train$hotel_cluster)
barplot(count)

# Count number of times each hotel_cluster is booked in the training data - to see most popular hotel
most.bookings <- aggregate(train$is_booking, by = list(train$hotel_cluster), sum)
barplot(most.bookings$x, names.arg = most.bookings$Group.1)

# Looking at most popular hotel_cluster by looking at % of times that it was booked
most.bookings.prop <- aggregate(train$is_booking, by = list(train$hotel_cluster), 
                                FUN = function(x) {sum(x)/length(x)})
barplot(most.bookings.prop$x, names.arg = most.bookings.prop$Group.1)

# Generate the number of times a hotel_cluster has appeared per destination
most.popular <- aggregate(train$is_booking, 
                          by = list(train$srch_destination_id, train$hotel_cluster), length)
most.popular <- most.popular[order(most.popular$Group.1, most.popular$x), ]
View(most.popular)

# Let's see if we can infer anything as well from the proportions of most.popular
temp <- aggregate(most.popular$x, by = list(most.popular$Group.1), sum)
most.popular.prop <- merge(most.popular, temp, by = "Group.1")
most.popular.prop$most.popular <- round(most.popular.prop$x.x / most.popular.prop$x.y, digits = 2)

#writing the file to the csv
write.csv(train, file="expediaData.csv")