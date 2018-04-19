
# sqrt tolerance
TOL <- .00000001

# Generate the next term in the Farey sequence
next.farey <- function() {
  x <<- trunc((y1+FAREY_MAX)/y2)*x2 -x1
  y <<- trunc((y1+FAREY_MAX)/y2)*y2 -y1
  x1 <<- x2
  x2 <<- x
  y1 <<- y2
  y2 <<- y

  c(x,y)
}

# Generates a pythagorean triple; m n are coprime, m > n, not both odd
triple <- function(k, m, n) {
  c(k*(m^2-n^2), k*2*m*n, k*(m^2+n^2))
}

# Check if a number is square
# Works on vectors and matrices too
is.square <- function(num) {
  (sqrt(num) - trunc(sqrt(num))) < TOL
}

write <- function() {
  write.csv(fareys, "~/Documents/polyvdw/fareys.csv", row.names=F)
  write.csv(triples, "~/Documents/polyvdw/triples.csv", row.names=F)
  write.csv(kfours, "~/Documents/polyvdw/kfours.csv", row.names=F)
}

# Check if any kfour is completed to the configuration by square
check.kfours <- function(kfours.sqd, square) {
  is.conf <- apply(kfours.sqd, 1, function(row) { check.row(row, square) })
  if(any(is.conf)) {
    print("WINNER!!")
    rows <- which(is.conf)
    for(row in rows) {
      tmp <- sqrt(kfours.sqd[row,])
      print(paste(tmp[1],tmp[2],tmp[3], sqrt(tmp[3]^2+square)))
    }
  }
}

# Add square to the largest (row[3])
# Then check if other differences are also square
check.row <- function(row, square) {
  w <- row[3]+square
  is.square(w-row[1]) & is.square(w-row[2])
}
