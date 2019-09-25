%%FRIDGE CONTENT

%%cibi disponibili
%%food(CODE, NAME, QUANTITY).
food(1,pasta,10).
food(2,carne,40).
getFood(L) :- findall(food(CODE, NAME, QUANTITY) , food(CODE, NAME, QUANTITY), L).
%%regole per modificare la base di conoscenza
updateQuantity(Code, Quantity) :- replaceRule( food(Code, Name, _ ) , food(Code, Name, Quantity)).
removeFood(Code) :- removeRule(food(Code, _ , _ )).
addFood(Code, Name, Quantity) :-  addRule( food(Code, Name,  Quantity)).