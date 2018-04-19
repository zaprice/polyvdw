
source('~/Documents/polyvdw/lib.R')

# Config vars
FAREY_MAX <- 10000
count <- 1
K_MAX <- 100

# Output vars
fareys <- matrix(nrow=0, ncol=2)
colnames(fareys) <- c("num", "denom")
triples <- matrix(nrow=0, ncol=4)
colnames(triples) <- c("a", "x", "y", "is.primitive")
kfours <- matrix(nrow=0, ncol=4)
colnames(kfours) <- c("x", "y", "z", "is.primitive")

# Farey var setup
x1 <- 0
y1 <- x2 <- 1
y2 <- FAREY_MAX
x <- y <- 0

# Farey loop
while(y != 1) {
  # Generate next Farey pair
  new.farey <- next.farey()
  fareys <- rbind(fareys, new.farey)
  # Skip rest if both are odd
  if(all((new.farey %% 2) == 1)) { next }

  # Loop over K param for Euclid's formula
  for(k in 1:K_MAX) {
    # Generate a triple with it
    new.triple <- sort(triple(k, new.farey[2], new.farey[1]))


    # Check against previous triples for K4s
    # You only need to check the first one for reasons
    overlaps <- which(triples[,1] == new.triple[1])
    for(i in overlaps) {
      over <- triples[i,]
      # Now we have b,c such that x^2 + b^2 = y^2 + c^2
      # Need to check if it is a square
      if(is.square(new.triple[2]^2 + over[3]^2)) {
        # Found a new K4
        new.kfour <- c(new.triple[2], new.triple[3], sqrt(new.triple[2]^2 + over[3]^2))
        kfours <- rbind(kfours, c(new.kfour, k))
      }
    }

    # Save, flag at the end if it's primitive
    triples <- rbind(triples, c(new.triple, as.numeric(k==1)))
  }

  # Write out results every 1000 usable Fareys
  if((count %% 1000) == 0) {
    write()
  }
  count <- count+1
}

write()
