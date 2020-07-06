# Sémantický slovník pojmů

Sémantický slovník pojmů je nástrojem určeným pro popis otevřených datových sad publikovaných veřejnou správou ČR. Architektura slovníku je popsána v této [koncepci](https://opendata.gov.cz/dokumenty:s%C3%A9mantick%C3%BD-slovn%C3%ADk-pojm%C5%AF:start).

## Struktura
Hlavním obsahem projektu jsou složky s jednotlivými slovníky ve formátu TTL jazyka RDF. Každý slovník má svoji kompaktní a plnou reprezentaci. Ty se liší v tom, zda jsou všechny slovníkové pojmy označující typy reprezentovány pomocí třídy jazyka OWL, nebo jsou pojmy označující typy vztahů reprezentovány jako objektové vlastnosti jazyka OWL. Obě reprezentace jsou vzájemně pojmově kompatibilní.
* `z-sgov` - složka s aktuální verzí základního slovníku (viz koncepce)
* `v-sgov` - složka s aktuální verzí slovníku veřejného sektoru (viz koncepce)
* `g-sgov` - kořenová složka pro generické slovníky
* `l-sgov` - kořenová složka pro legislativní slovníky
* `a-sgov` - kořenová složka pro agendové slovníky
* `d-sgov` - kořenová složka pro datové slovníky

## Přístup k slovníku
Tento GIT repozitář je zdrojem pro následující aplikace nad slovníkem:
* [Prohlížeč](https://slovník.gov.cz/prohlížeč) - fasetový vyhledávač pojmů napříč slovníky
* [SPARQL Endpoint](https://slovník.gov.cz/sparql)

Tento repozitář je udržován v rámci projektu OPZ č. CZ.03.4.74/0.0/0.0/15_025/0004172.
![Evropská unie - Evropský sociální fond - Operační program Zaměstnanost](https://data.gov.cz/images/ozp_logo_cz.jpg)
 