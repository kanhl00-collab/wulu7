isVarExpr(constE(_)) :- !.
isVarExpr(absE(A)) :- isVarExpr(A), !.
isVarExpr(negE(A)) :- isVarExpr(A), !.
isVarExpr(plusE(A,B)) :- isVarExpr(A), isVarExpr(B), !.
isVarExpr(minusE(A,B)) :- isVarExpr(A), isVarExpr(B), !.
isVarExpr(timesE(A,B)) :- isVarExpr(A), isVarExpr(B), !.
isVarExpr(expE(A,B)) :- isVarExpr(A), isVarExpr(B).
isVarExpr(subst(A, B, C)) :- isVarExpr(A), atom(B), isVarExpr(C).
isVarExpr(var(A)) :- atom(A).

interpretExpr(constE(A),A).
interpretExpr(var(A),var(A)).
interpretExpr(absE(A),X) :- interpretExpr(A,Y), (Y > 0 -> X is Y; X is -1 * Y).
interpretExpr(negE(A),X) :- interpretExpr(A,Y), X is -1 * Y.
interpretExpr(plusE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y + Z.
interpretExpr(minusE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y - Z.
interpretExpr(timesE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y * Z.
interpretExpr(expE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y ** Z.
interpretExpr(subst(A,B,C),X) :- substitute(A,B,C,Y), interpretExpr(Y,X).

interpretVarExpr(subst(A,B,C),X) :- has(A,B), substitute(A,B, C,Y), interpretExpr(Y,X).
interpretVarExpr(A,X) :- interpretExpr(A,X).

has(constE(_),_) :- false.
has(var(A),A).
has(absE(A),B) :- has(A,B).
has(negE(A),B) :- has(A,B).
has(plusE(A,D),B) :- has(A,B); has(D,B).
has(minusE(A,D),B) :- has(A,B); has(D,B).
has(timesE(A,D),B) :- has(A,B); has(D,B).
has(expE(A,D),B) :- has(A,B); has(D,B).
has(subst(A,D,E),B) :- has(A,D), substitute(A,D,E,Y), has(Y,B).



substitute(constE(A),_,_,constE(A)).
substitute(var(B),B, C, C) :- !.
substitute(var(A),_, _, var(A)) :- !.
substitute(absE(A),B, C, X) :- substitute(A,B,C,Y), interpretExpr(absE(Y),X).
substitute(negE(A),B, C, X) :- substitute(A,B,C,Y), interpretExpr(negE(Y),X).
substitute(plusE(A,D),B, C, plusE(Y,D)) :- has(A,B), !, substitute(A,B,C,Y).
substitute(plusE(A,D),B, C, plusE(A,Z)) :- has(D,B), substitute(D,B,C,Z).
substitute(minusE(A,D),B, C, minusE(Y,D)) :- has(A,B), !, substitute(A,B,C,Y).
substitute(minusE(A,D),B, C, minusE(A,Z)) :- has(D,B), substitute(D,B,C,Z).
substitute(timesE(A,D),B, C, timesE(Y,D)) :- has(A,B), !, substitute(A,B,C,Y).
substitute(timesE(A,D),B, C, timesE(A,Z)) :- has(D,B), substitute(D,B,C,Z).
substitute(expE(A,D),B, C, expE(Y,D)) :- has(A,B), !, substitute(A,B,C,Y).
substitute(expE(A,D),B, C, expE(A,Z)) :- has(D,B), substitute(D,B,C,Z).
substitute(subst(A,D,E),B,C,X) :- substitute(A,B,C,Y), substitute(Y,D,E,X).

