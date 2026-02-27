hasDivisorLessThanOrEqualTo(_,1) :- !, false.
hasDivisorLessThanOrEqualTo(X,Y) :- 0 is X mod Y, !.
hasDivisorLessThanOrEqualTo(X,Y) :- Z is Y - 1, hasDivisorLessThanOrEqualTo(X,Z).

isPrime(1) :- !, false.

isPrime(X) :- Y is X-1, \+ hasDivisorLessThanOrEqualTo(X, Y).


isDigitList(_,[]) :- false.
isDigitList(X,[X]):- X<10, !. % Your code here; change the . to :-
isDigitList(X, [H|T]) :-
  X2 is X div 10,
  H is X mod 10,
  isDigitList(X2,T),
  !.
%isDigitList(X, [H|T]) :-
  %last(L,F),
  % F is the final element of the list
  %X2 is (X-F)/10,
  %dropLast(L,L2),
  %isDigitList(X2,L2),
  %isDigitList(F,[F]).
  % Your code here; change the above . to a ,

  %isDigitList(X2,T),
  %X is X2 * 10 + H.



% dropLast(L1,L2) if L2 is the list L1, leaving off the last item.
dropLast([_],[]) :- !. % The last element is dropped.
dropLast([H|T],[H|T2]) :-
  % Aside from the base case above, the lists must match.
  dropLast(T,T2).


isPalindrome([]):- !. %empty list
isPalindrome([_]) :- !. %single element
isPalindrome([H|T]) :-
  last(T, L),
  H = L,  %head and last equals
  dropLast(T, T2),  % the middle is palindrome
  isPalindrome(T2).% H is the first element, T is the rest


primePalindrome(X) :-
  isPrime(X),
  isDigitList(X,L),
  isPalindrome(L).
