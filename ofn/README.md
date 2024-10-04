# Transformace do struktury OFN

Tento projekt obsahuje nástroje pro transformaci SSP slovníků do formátu [OFN Slovníky](https://ofn.gov.cz/slovn%C3%ADky/).

* Zatím je implementována pouze logika týkající se glosářů *
- pravidla transformace jsou implementována jako sada [SPARQL Update dotazů](./src/main/resources/updates).
- [SHACL profil](./src/main/resources/ofn-slovniky-shacl.ttl) pro validaci slovníku dle OFN, který je testován na [snippetech z OFN](src/test/resources/cz.gov.ssp.ofn.valid) a [vlastních příkladech](src/test/resources/cz.gov.ssp.ofn.invalid)
- výsledný [VSGoV glosář](../content/vocabularies/v-sgov/v-sgov-glos%C3%A1%C5%99-ofn.ttl)