
source("~/Documents/polyvdw/src/R/lib.R")

SQUARE_MAX <- 10000

# Check if any of the K3s we found could be the winning configuration
kthrees.sqd <- (read.csv("~/Documents/polyvdw/kthrees.csv")[,1:3])^2
squares <- (1:SQUARE_MAX)^2

for(square in squares) {
  check.kthrees(kthrees.sqd, square)
}
