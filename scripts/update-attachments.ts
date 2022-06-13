// TO RUN THIS FROM ROOT:
// deno run --allow-read --allow-write ./scripts/update-attachments.ts

import {
  expandGlob,
  type WalkEntry,
} from "https://deno.land/std@0.143.0/fs/mod.ts";

const PP_CONTEXT = "https://slovník.gov.cz/datový/pracovní-prostor/pojem/";
const PD_CONTEXT =
  "http://onto.fel.cvut.cz/ontologies/slovník/agendový/popis-dat/pojem/";

async function main() {
  for await (const vocabulary of expandGlob("./content/vocabularies/*")) {
    await updateVocabulary(vocabulary);
  }
  for await (const attachment of expandGlob("./content/attachments/*")) {
    await updateAttachment(attachment);
  }
}

async function updateVocabulary(vocabularyFolder: WalkEntry) {
  const attachmentsFile = `${vocabularyFolder.path}/${vocabularyFolder.name}-přílohy.ttl`;
  await replaceNamespaceInFile(attachmentsFile);
}

async function updateAttachment(attachmentFolder: WalkEntry) {
  const attachmentsFile = `${attachmentFolder.path}/${attachmentFolder.name}-příloha.ttl`;
  await replaceNamespaceInFile(attachmentsFile);
}

async function replaceNamespaceInFile(fileName: string) {
  try {
    const fileContent = await Deno.readTextFile(fileName);
    console.log(`Replacing namespaces in ${fileName}`);

    const updatedFileContent = fileContent.replaceAll(PP_CONTEXT, PD_CONTEXT);

    await Deno.writeTextFile(fileName, updatedFileContent);
  } catch (e) {
    if (!(e instanceof Deno.errors.NotFound)) {
      // ignore errors if the attachments file is not present, throw otherwise
      throw e;
    }
  }
}

// run!
main();
