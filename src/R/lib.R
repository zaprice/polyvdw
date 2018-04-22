
# sqrt tolerance
TOL <- .000000000001

#######################
# Objects
#######################

# Initialize output objects
init <- function() {
  triples <<- new.env(hash=T)
  kthrees <<- list()
  kthrees$mat <<- matrix(nrow=30, ncol=4)
  colnames(kthrees$mat) <<- c("x", "y", "z", "is.primitive")

  kthrees$row <<- 1
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
# In sorted order, swapping a,b as determined by the params
triple <- function(k, m, n) {
  if(m/n - n/m > 2) {
    c(k*2*m*n, k*(m^2-n^2), k*(m^2+n^2))
  } else {
    c(k*(m^2-n^2), k*2*m*n, k*(m^2+n^2))
  }
}

# Check if a number is square
# Works on vectors and matrices too
is.square <- function(num) {
  (sqrt(num) - trunc(sqrt(num))) < TOL
}

# Check if any kthree is completed to the configuration by square
check.kthrees <- function(kthrees.sqd, square) {
  is.conf <- apply(kthrees.sqd, 1, function(row) { check.row(row, square) })
  if(any(is.conf)) {
    print("WINNER!!")
    rows <- which(is.conf)
    for(row in rows) {
      tmp <- sqrt(kthrees.sqd[row,])
      conf <- c(tmp[1],tmp[2],tmp[3], tmp[3]^2+square)
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

# Check if vector v is a Pythagorean Kn
is.pyth <- function(v) {
  v <- v^2
  for(i in 1:length(v)) {
    for(j in i:length(v)) {
      print(sqrt(v[j] - v[i]))
      if(!is.square(v[j] - v[i])) {
        return(F)
      }
    }
  }
  T
}

#######################
# Other
#######################

write <- function() {
  write.csv(kthrees$mat[1:(kthrees$row-1),], "~/Documents/polyvdw/kthrees.csv", row.names=F)
}
