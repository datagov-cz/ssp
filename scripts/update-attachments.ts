// TO RUN THIS FROM ROOT:
// deno run --allow-read --allow-write ./scripts/update-attachments.ts

import {
  expandGlob,
  type WalkEntry,
} from "https://deno.land/std@0.143.0/fs/mod.ts";

const SGOV_CONTEXT = "https://slovník.gov.cz/";
const PP_CONTEXT = "https://slovník.gov.cz/datový/pracovní-prostor/pojem/";
const PD_CONTEXT =
  "http://onto.fel.cvut.cz/ontologies/slovník/agendový/popis-dat/pojem/";

const PP_HAS_ATTACHMENT = `<${PP_CONTEXT}má-přílohu>`;
const PD_HAS_ATTACHMENT = `<${PD_CONTEXT}má-přílohu>`;

const PP_ATTACHMENT_PREFIX = `<${PP_CONTEXT}příloha/`;
const SGOV_ATTACHMENT_PREFIX = `<${SGOV_CONTEXT}příloha/`;

const PP_ATTACHMENT_TYPE_REGEX = new RegExp(
  ",\\s+<https://slovník.gov.cz/datový/pracovní-prostor/pojem/příloha>;",
  "g"
);
const PP_ATTACHMENT_TYPE_REPLACEMENT = ".";

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
  await replaceInFile(attachmentsFile, PP_HAS_ATTACHMENT, PD_HAS_ATTACHMENT);
  await replaceInFile(
    attachmentsFile,
    PP_ATTACHMENT_PREFIX,
    SGOV_ATTACHMENT_PREFIX
  );
}

async function updateAttachment(attachmentFolder: WalkEntry) {
  const attachmentsFile = `${attachmentFolder.path}/${attachmentFolder.name}-příloha.ttl`;
  await replaceInFile(
    attachmentsFile,
    PP_ATTACHMENT_PREFIX,
    SGOV_ATTACHMENT_PREFIX
  );
  await replaceInFile(
    attachmentsFile,
    PP_ATTACHMENT_TYPE_REGEX,
    PP_ATTACHMENT_TYPE_REPLACEMENT
  );
}

async function replaceInFile(
  fileName: string,
  from: string | RegExp,
  to: string
) {
  try {
    const fileContent = await Deno.readTextFile(fileName);
    console.log(`Replacing namespaces in ${fileName}`);

    const updatedFileContent = fileContent.replaceAll(from, to);

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
