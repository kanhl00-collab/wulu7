isExpr(constE(_)) :- !.
isExpr(absE(A)) :- isExpr(A), !.
isExpr(negE(A)) :- isExpr(A), !.
isExpr(plusE(A,B)) :- isExpr(A), isExpr(B).
isExpr(minusE(A,B)) :- isExpr(A), isExpr(B).
isExpr(timesE(A,B)) :- isExpr(A), isExpr(B).
isExpr(expE(A,B)) :- isExpr(A), isExpr(B).

interpretExpr(constE(A),X) :- X is A.
interpretExpr(absE(A),X) :- interpretExpr(A,Y), (Y > 0 -> X is Y; X is -1 * Y).
interpretExpr(negE(A),X) :- interpretExpr(A,Y), X is -1 * Y.
interpretExpr(plusE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y + Z.
interpretExpr(minusE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y - Z.
interpretExpr(timesE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y * Z.
interpretExpr(expE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y ** Z.
