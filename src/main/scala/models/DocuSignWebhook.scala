package models

case class DocuSignWebhook(
  event: String,
  apiVersion: String,
  uri: String,
  retryCount: Int,
  configurationId: Int,
  generatedDateTime: String,
  data: WebhookData
)

case class WebhookData(
  accountId: String,
  userId: String,
  envelopeId: String,
  envelopeSummary: EnvelopeSummary
)

case class EnvelopeSummary(
  status: String,
  documentsUri: String,
  recipientsUri: String,
  attachmentsUri: String,
  envelopeUri: String,
  emailSubject: String,
  envelopeId: String,
  signingLocation: String,
  customFieldsUri: String,
  notificationUri: String,
  enableWetSign: String,
  allowMarkup: String,
  allowReassign: String,
  createdDateTime: String,
  lastModifiedDateTime: String,
  deliveredDateTime: String,
  initialSentDateTime: String,
  sentDateTime: String,
  completedDateTime: String,
  statusChangedDateTime: String,
  documentsCombinedUri: String,
  certificateUri: String,
  templatesUri: String,
  expireEnabled: String,
  expireDateTime: String,
  expireAfter: String,
  sender: Sender,
  customFields: CustomFields,
  recipients: Recipients,
  envelopeDocuments: List[EnvelopeDocument]
)

case class Sender(
  userName: String,
  userId: String,
  accountId: String,
  email: String,
  ipAddress: String
)

case class CustomFields(
  textCustomFields: List[TextCustomField],
  listCustomFields: List[String]
)

case class TextCustomField(
  fieldId: String,
  name: String,
  show: String,
  required: String,
  value: String
)

case class Recipients(
  signers: List[Signer],
  agents: List[String],
  editors: List[String],
  intermediaries: List[String],
  carbonCopies: List[String],
  certifiedDeliveries: List[String],
  inPersonSigners: List[String],
  seals: List[String],
  witnesses: List[String],
  notaries: List[String],
  recipientCount: String,
  currentRoutingOrder: String
)

case class Signer(
  signatureInfo: SignatureInfo,
  tabs: Tabs,
  creationReason: String,
  isBulkRecipient: String,
  requireUploadSignature: String,
  name: String,
  firstName: String,
  lastName: String,
  email: String,
  recipientId: String,
  recipientIdGuid: String,
  requireIdLookup: String,
  userId: String,
  clientUserId: String,
  routingOrder: String,
  note: String,
  roleName: String,
  status: String,
  completedCount: String,
  signedDateTime: String,
  deliveredDateTime: String,
  sentDateTime: String,
  deliveryMethod: String,
  totalTabCount: String,
  recipientType: String
)

case class SignatureInfo(
  signatureName: String,
  signatureInitials: String,
  fontStyle: String
)

case class Tabs(
  signHereTabs: List[SignHereTab],
  fullNameTabs: List[FullNameTab],
  dateSignedTabs: List[DateSignedTab],
  textTabs: List[TextTab],
  emailAddressTabs: List[EmailAddressTab]
)

case class SignHereTab(
  stampType: String,
  name: String,
  tabLabel: String,
  scaleValue: String,
  optional: String,
  documentId: String,
  recipientId: String,
  pageNumber: String,
  xPosition: String,
  yPosition: String,
  tabId: String,
  templateRequired: String,
  status: String,
  tabType: String,
  tooltip: String,
  agreementAttributeLocked: String
)

case class FullNameTab(
  name: String,
  value: String,
  tabLabel: String,
  font: String,
  fontColor: String,
  fontSize: String,
  documentId: String,
  recipientId: String,
  pageNumber: String,
  xPosition: String,
  yPosition: String,
  width: String,
  height: String,
  tabId: String,
  templateRequired: String,
  tabType: String,
  tooltip: String,
  agreementAttributeLocked: String
)

case class DateSignedTab(
  name: String,
  value: String,
  tabLabel: String,
  font: String,
  fontColor: String,
  fontSize: String,
  documentId: String,
  recipientId: String,
  pageNumber: String,
  xPosition: String,
  yPosition: String,
  width: String,
  height: String,
  tabId: String,
  templateRequired: String,
  tabType: String,
  agreementAttributeLocked: String
)

case class TextTab(
  value: String,
  originalValue: String,
  required: String,
  locked: String,
  concealValueOnDocument: String,
  disableAutoSize: String,
  maxLength: String,
  tabLabel: String,
  font: String,
  fontColor: String,
  fontSize: String,
  documentId: String,
  recipientId: String,
  pageNumber: String,
  xPosition: String,
  yPosition: String,
  width: String,
  height: String,
  tabId: String,
  templateRequired: String,
  tabType: String
)

case class EmailAddressTab(
  name: String,
  value: String,
  tabLabel: String,
  font: String,
  fontColor: String,
  fontSize: String,
  documentId: String,
  recipientId: String,
  pageNumber: String,
  xPosition: String,
  yPosition: String,
  width: String,
  height: String,
  tabId: String,
  templateRequired: String,
  tabType: String,
  tooltip: String,
  agreementAttributeLocked: String
)

case class EnvelopeDocument(
  documentId: String,
  documentIdGuid: String,
  name: String,
  `type`: String,
  uri: String,
  order: String,
  pages: List[Page],
  display: String,
  includeInDownload: String,
  signerMustAcknowledge: String,
  templateRequired: String,
  authoritativeCopy: String,
  PDFBytes: String
)

case class Page(
  pageId: String,
  sequence: String,
  height: String,
  width: String,
  dpi: String
)
