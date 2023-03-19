# Students-Electronic-Class-Book

## Mod de implementare

Am implementat programul în ordinea în care este descris în enunțul temei, gândind la 
fiecare pas integrarea noilor clase cu funcționalitățile oferite de cele deja existente.
Pentru a face posibil acest lucru, am adăugat diverse câmpuri sau funcții în cadrul claselor, 
acolo unde am considerat că acest lucru ar fi util. Voi oferi mai jos câteva detalii despre 
lucruri adăugate sau alegeri făcute pe parcursul implementării:

### Clasa *Catalog*

Am adăugat la clasa *Catalog* obiecte de tip `TreeSet` (mulțimi) pentru:
- observatorii catalogului (părinții studenților)
- studenții creați în program
- profesorii creați în program
- un obiect de tip *ScoreVisitor* asociat profesorilor și asistenților

De asemenea, pentru această clasă am folosit varianta „lazy instantiation” pentru șablonul 
`Singleton`, considerând că programul nostru va rula pe un singur thread.

### Clasa *Course*

Pentru clasa *Course*, am adăugat:
- un obiect de tip *Strategy*, pentru a reține strategia cursului

Pentru clasa *CourseBuilder*, am considerat drept câmpuri strict necesare construirii unui nou 
obiect de tip *Course* următoarele câmpuri:
- name
- teacher
- points

De asemenea, funcția *addAssistant* din clasa *Course* are exclusiv funcție de setare a unui 
asistent pentru o grupă existentă, după cum este descris în enunțul temei.

### Interfața grafică

Am construit paginile `Student Page`, `Teacher / Assistant Page`, respectiv `Parents Page`, 
pentru a oferi detalii despre orice student, profesor, asistent sau părinte adăugat în 
program, prin posibilitatea selectării utilizatorului dorit dintr-o listă cu toți utilizatorii de 
acest tip.
- clasa *PageStudent*
- clasa *PageTeacherAssistant*
- clasa *PageParent*

Pentru `Teacher / Assistant Page` am creat o pagină unică, ce conține două liste, de profesori 
și asistenți, din care poate fi selectat un singur utilizator la un moment dat, iar în funcție de 
tipul și de numele său se afișează datele corespunzătoare, cu posibilitatea de validare a 
notelor.

Am implementat și clase pentru construirea de pagini asociate unui singur utilizator din 
fiecare tip, primit ca parametru:
- clasa *SingleStudentPage*
- clasa *SingleTeacherPage*
- clasa *SingleAssistantPage*
- clasa *SingleParentPage*

### Bonus

Am implementat cerințele de la punctul 3.1, descrise în secțiunea Bonus:
- clasa *AuthPage*

>Pagina oferă o metodă de autentificare pe baza selectării tipului de utilizator și 
oferirea numelui, respectiv prenumelui persoanei. Se verifică existența în cadrul 
programului a unui utilizator ce îndeplinește condițiile date, iar, dacă s-a găsit acest 
utilizator, se deschide o fereastră cu pagina asociată acestuia.

- clasa *CourseOperationsPage*

>Pagina oferă date principale ale cursului, trei liste cu studenți, note și asistenți 
aferenți acestui curs, împreună cu posibilitatea de adăugare de studenți la curs, de 
note, asistenți sau noi grupe. Se oferă de asemenea și câteva statistici asociate 
cursului, precum mediile notelor sau numele celui mai bun student, pe baza 
strategiei alese de profesor.

- clasa *AddCoursePage*

>Pagina pune la dispoziție un câmp text în care se introduce calea către un fișier `.json` 
cu date principale necesare construirii unui nou obiect de tip *Course*. Dacă s-a găsit 
fișierul și s-a putut construi un nou curs, se deschide o pagină *CourseOperationsPage* 
aferentă acestuia, în cadrul căreia se pot executa operații de adăugare / modificare, 
după cum s-a descris mai sus.

Pentru construirea paginilor, am folosit fie `GridLayout`, fie `GridBagLayout`.

## Testare

Am testat programul prin construirea clasei *OfficialTestClass*, care preia datele din fișierul 
`.json` oferit ca suport, `catalog.json`, construind obiectele aferente pe baza acestora. Am 
verificat apoi funcționarea programului prin intermediul interfeței grafice.
