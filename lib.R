
# sqrt tolerance
TOL <- .000000000001

#######################
# Objects
#######################

# Initialize output objects
init <- function() {
  fareys <<- list()
  triples <<- list()
  kfours <<- list()

  fareys$mat <<- matrix(nrow=5000, ncol=2)
  colnames(fareys$mat) <<- c("num", "denom")
  triples$mat <<- matrix(nrow=100000, ncol=4)
  colnames(triples$mat) <<- c("a", "x", "y", "is.primitive")
  kfours$mat <<- matrix(nrow=30, ncol=4)
  colnames(kfours$mat) <<- c("x", "y", "z", "is.primitive")

  fareys$row <<- 1
  triples$row <<- 1
  kfours$row <<- 1
}

# Add new row to mat object, resizing if necessary
addnew <- function(mat, new) {
  mat$mat[mat$row,] <- new
  mat$row <- mat$row +1
  if(mat$row > nrow(mat$mat)) {
    mat$mat <- rbind(mat$mat, matrix(nrow=nrow(mat$mat), ncol=ncol(mat$mat)))
  }
  return(mat)
}

#######################
# Math Stuff
#######################

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

# Check if any kfour is completed to the configuration by square
check.kfours <- function(kfours.sqd, square) {
  is.conf <- apply(kfours.sqd, 1, function(row) { check.row(row, square) })
  if(any(is.conf)) {
    print("WINNER!!")
    rows <- which(is.conf)
    for(row in rows) {
      tmp <- sqrt(kfours.sqd[row,])
      conf <- c(tmp[1],tmp[2],tmp[3], sqrt(tmp[3]^2+square))
      print(paste(conf))
    }
  }
}

# Add square to the largest (row[3])
# Then check if other differences are also square
check.row <- function(row, square) {
  w <- row[3]+square
  is.square(w-row[1]) & is.square(w-row[2])
}

#######################
# Other
#######################

write <- function() {
  write.csv(fareys$mat[1:(fareys$row-1),], "~/Documents/polyvdw/fareys.csv", row.names=F)
  write.csv(triples$mat[1:(triples$row-1),], "~/Documents/polyvdw/triples.csv", row.names=F)
  write.csv(kfours$mat[1:(kfours$row-1),], "~/Documents/polyvdw/kfours.csv", row.names=F)
}
