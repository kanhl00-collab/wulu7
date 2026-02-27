isExpr(constE(_)) :- !.
isExpr(absE(A)) :- isExpr(A), !.
isExpr(negE(A)) :- isExpr(A), !.
isExpr(plusE(A,B)) :- isExpr(A), isExpr(B), !.
isExpr(minusE(A,B)) :- isExpr(A), isExpr(B), !.
isExpr(timesE(A,B)) :- isExpr(A), isExpr(B), !.
isExpr(expE(A,B)) :- isExpr(A), isExpr(B).

isBoolExpr(tt).
isBoolExpr(ff).
isBoolExpr(band(A,B)) :- isBoolExpr(A), isBoolExpr(B).
isBoolExpr(bnot(A)) :- isBoolExpr(A).
isBoolExpr(bor(A,B)) :- isBoolExpr(A), isBoolExpr(B).

isMixedExpr(A) :- isExpr(A); isBoolExpr(A).

interpret(constE(A),X) :- X is A.
interpret(absE(A),X) :- interpret(A,Y), (Y > 0 -> X is Y; X is -1 * Y).
interpret(negE(A),X) :- interpret(A,Y), X is -1 * Y.
interpret(plusE(A,B),X) :- interpret(A,Y), interpret(B,Z), X is Y + Z.
interpret(minusE(A,B),X) :- interpret(A,Y), interpret(B,Z), X is Y - Z.
interpret(timesE(A,B),X) :- interpret(A,Y), interpret(B,Z), X is Y * Z.
interpret(expE(A,B),X) :- interpret(A,Y), interpret(B,Z), X is Y ** Z.

interpretBool(tt).
interpretBool(ff) :- false.
interpretBool(band(A,B)) :- interpretBool(A), interpretBool(B).
interpretBool(bor(A,B)) :- interpretBool(A); interpretBool(B).
interpretBool(bnot(A)) :- \+ interpretBool(A).

interpretBoolExpr(A,true) :- (interpretBool(A) -> true; false).
interpretBoolExpr(A,false) :- (interpretBool(A) -> false; true).

interpretMixedExpr(A, X) :- interpret(A,X); interpretBoolExpr(A,X).
