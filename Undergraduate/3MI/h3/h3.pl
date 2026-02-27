binFlatten(empty,[]).
binFlatten(node(L,A,R),B) :-
    binFlatten(L,B1),
    binFlatten(R,B2),
    append(B1,[A],B3),
    append(B3,B2,B).

leafFlatten(leaf(A),[A]).
leafFlatten(branch(L,R), B) :-
    leafFlatten(L,B1),
    leafFlatten(R,B2),
    append(B1,B2,B).

merge(X,[],X).
merge([],Y,Y).
merge([H1|T1],[H2|T2],Z):-
    (H1 < H2
     -> merge(T1,[H2|T2],Z2),
     append([H1],Z2,Z);
     merge([H1|T1],T2,Z3),
     append([H2],Z3,Z)).
    
binElemsOrdered(empty,[]).
binElemsOrdered(node(L,A,R),B):-
    binElemsOrdered(L,L1),
    binElemsOrdered(R,R1),
    merge(L1,[A],M),
    merge(M,R1,B).

leafElemsOrdered(leaf(A),[A]).
leafElemsOrdered(branch(L,R),B) :-
    leafElemsOrdered(L,L1),
    leafElemsOrdered(R,R1),
    merge(L1,R1,B).

isBinTree(empty).
isBinTree(node(L,_,R)) :- isBinTree(L), isBinTree(R).

isLeafTree(leaf(_)).
isLeafTree(branch(L,R)) :- isLeafTree(L), isLeafTree(R).

flatten(T,L) :-
    isBinTree(T),
    !,
    binFlatten(T,L).
flatten(T,L) :-
    isLeafTree(T),
    !,
    leafFlatten(T,L).

elemsOrdered(T,L) :-
    isBinTree(T),
    !,
    binElemsOrdered(T,L).
elemsOrdered(T,L) :-
    isLeafTree(T),
    !,
    leafElemsOrdered(T,L).
