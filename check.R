
source("~/Documents/polyvdw/lib.R")

SQUARE_MAX <- 10000

# Check if any of the K4s we found could be the winning configuration
kfours.sqd <- (read.csv("~/Documents/polyvdw/kfours.csv")[,1:3])^2
squares <- (1:SQUARE_MAX)^2

for(square in squares) {
  check.kfours(kfours.sqd, square)
}