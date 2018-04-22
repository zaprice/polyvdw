
library(profvis)
source('~/Documents/polyvdw/src/R/lib.R')

# Config vars
FAREY_MAX <- 700
count <- 1
K_MAX <- 100

# Initialize output vars
init()

# Farey var setup
x1 <- 0
y1 <- x2 <- 1
y2 <- FAREY_MAX
x <- y <- 0

# Farey loop
while(y != 1) {
  # Generate next Farey pair
  new.farey <- next.farey()

  # Skip rest if both are odd
  if(all((new.farey %% 2) == 1)) { next }

  # Loop over K param for Euclid's formula
  for(k in 1:K_MAX) {
    # Generate a triple with it
    new.triple <- triple(k, new.farey[2], new.farey[1])


    # Check against previous triples for K3s
    # You only need to check the first one for reasons
    ind <- as.character(new.triple[1])
    if(is.null(triples[[ind]])) {
      triples[[ind]] <- list(new.triple)
    } else {
      for(over in triples[[ind]]) {
        if(is.square(new.triple[2]^2 + over[3]^2)) {
          # Found a new K3
          new.kthree <- c(new.triple[2], new.triple[3], sqrt(new.triple[2]^2 + over[3]^2))
          kthrees <- addnew(kthrees, c(new.kthree, k))
        }
      }
      triples[[ind]][[length(triples[[ind]])+1]] <- new.triple
    }
  }

  # Write out results every 1000 usable Fareys
  if((count %% 1000) == 0) {
    write()
  }
  count <- count+1
}

write()
