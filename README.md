# XML validátor

## Feladat:
Készíteni kellett egy XSD fájlt és ennek megfelelően 3 darab valid és 3 db nem valid XML fájlt. Egy Java kódot is kellett mellé írni, amelynek paraméterként meg lehet adni az XML fájlokat tartalmazó directory-t és XSD fájl nevét. A program kiolvassa az összes XML fájlt a directory-ból és szerre levalidálja azokat. Az eredményt kiírja egy txt fájlba, aminek a nevét az év, hónap, nap, óra, perc, másodperc alapján képezi, következő formában:
> YYYY-MM-DD_HH-mm-ss.txt

Az XSD és XML fájlok az __xml_documents/__ nevű mappában találhatók. Az elnevezésükben olvasható, hogy mely fájlok azok, amikre azt kell kiadja a program, hogy helyes és mely fájlok azok, amikre ki kell írja a hibákat.

## XSD séma definíció
A séma a következő megszorításokat helyezi egy XML fájl elé, ahhoz, hogy validnak lehessen mondani:
- *dokumentumCim*           - string, hossza 1-től 200 karakterig terjedő lehet
- *dokumentumSzovegesSzama* - egész szám, ami nem lehet kisebb, mint 0
- *dokumentumEvszama*       - egész szám, ami 1900-2100-ig vehet fel értékeket
- *dokumentumSorszama*      - egész szám, ami nem lehet kisebb mint 0
- *dokumentumTipusa*        - egy enum. Értéke lehet: COM, JOIN, SEC, SWD
- *kibocsatoKod*            - egy enum. Értéke lehet: EB, fk
- *kibocsatasDatuma*        - egy dátum, ami 1900-01-01-től 2100-01-01-ig vehet fel értékeket
- *dokumentumTartalom*      - szakaszokat tartalmaz. Legalább egy szakasz kell legyen a dokumentumban
- *szakasz*                 - bekezdéseket tartalmaz. Legalább egy bekezdés kell legyen a dokumentumban
- *bekezdes*                - egy szöveg

## Java kód
Maven build tool-t használtam, hogy a jar fájl létrehozása könnyebb legyen. A __pom.xml__-ben meg van adva, hogy 1.8-as java-t használjon, jar-t hozzon létre, és hogy a végső neve a jar-nak *XMLValidator* legyen. 

A main osztály a belépési pont(mit ad Isten :) ). Először is ellenőrzi, hogy vannak-e paraméterek. Ha nincsenek, akkor leáll és kiírja, hogy hogyan kell elindítani a programot:
```
   Usage: java -jar <program_name> <directory_of_xmls> <xsd_filename>
   Default program name: XMLValidator.jar 
```

Ha helyesen van elindítva, akkor létrehoz egy *FileProcessor* objektumot. A *FileProcessor* konstruktora ellenőrzi, hogy tényleg egy mappa és egy fájl van-e megadva, aminek a kiterjesztése a paraméterként átadott extension(jelen esetben .xsd). Ha valami nem stimmel, akkor kivételt dob és leáll a program a megfelelő hibaüzenet kiírásával. 
Ha sikeresen létrejött a __fileProcessor__ objektum, akkor lekéri az adott mappában levő xml file-ok listáját. 
Mindezek után létrejön egy __XmlValidator__ objektum, ha az átadott xsd fájl létezik a megadott útvonallal és névvel. Ha nem fájl lett megadva, hanem directory, akkor itt is kivételt dobódik, azzal az üzenettel, hogy nem létezik az adott fájl. 
A megkapott XML fájl listán végigmenve, mindegyik XML fájlt validálja, és beírja a paraméterként kapott fájlba a validálás eredményét. Ha egy XML fájl nem valid, akkor a hiba okát is beleírja. Mindezek után kiírja a sikeres lefutás üzenetét.

# Maven rész
A következő parancs kiadásával létrehoz egy jar állományt a következő névvel: *XMLValidator.jar*
``` 
   mvn clean package 
```

Megvárjuk a lefutását a parancsnak. Létrehoz egy __/target__ nevű mappát. Ebbe a mappába teszi bele a létrehozott jar-t.
Ezt a jar-t ezután mozgathatjuk. A parancs, amit ki kell adni, hogy máshol is lefusson(ha fel van telepítve a JDK):
```
   java -jar XMLValidator.jar <XML_mappa_név> <XSD_fájl>
```

A létrehozott fájlt abban a mappában kell keresni, ahol az XML fájlok is vannak.
