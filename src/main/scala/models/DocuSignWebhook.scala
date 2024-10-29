package models

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class DocuSignWebhook(
  event: String,
  apiVersion: String,
  uri: String,
  retryCount: Int,
  configurationId: Long,
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
  signingLocation: String,
  customFieldsUri: String,
  notificationUri: String,
  enableWetSign: String,
  allowMarkup: String,
  allowReassign: String,
  createdDateTime: String,
  lastModifiedDateTime: String,
  deliveredDateTime: String,
  sentDateTime: String,
  completedDateTime: String,
  statusChangedDateTime: String,
  documentsCombinedUri: String,
  certificateUri: String,
  templatesUri: String,
  sender: DocuSignSender,
  recipients: Recipients,
  envelopeDocuments: List[DocuSignDocument],
  customFields: CustomFields
)

case class DocuSignSender(
  userName: String,
  userId: String,
  accountId: String,
  email: String,
  ipAddress: String
)

case class Recipients(
  signers: List[Signer],
  recipientCount: String,
  currentRoutingOrder: String
)

case class Signer(
  name: String,
  email: String,
  recipientId: String,
  recipientIdGuid: String,
  status: String,
  signedDateTime: String,
  deliveredDateTime: String,
  deliveryMethod: String,
  recipientType: String
)

case class DocuSignDocument(
  documentId: String,
  documentIdGuid: String,
  name: String,
  `type`: String,
  uri: String,
  order: String,
  pages: List[DocumentPage],
  display: String,
  includeInDownload: String,
  signerMustAcknowledge: String,
  templateRequired: String,
  authoritativeCopy: String,
  PDFBytes: String
)

case class DocumentPage(
  pageId: String,
  sequence: String,
  height: String,
  width: String,
  dpi: String
)

case class CustomFields(
  textCustomFields: List[TextCustomField]
)

case class TextCustomField(
  name: String,
  value: String,
  fieldId: String,
  show: String,
  required: String
)

object DocuSignWebhook {
  implicit val decoder: Decoder[DocuSignWebhook] = deriveDecoder[DocuSignWebhook]
  implicit val encoder: Encoder[DocuSignWebhook] = deriveEncoder[DocuSignWebhook]
}

object WebhookData {
  implicit val decoder: Decoder[WebhookData] = deriveDecoder[WebhookData]
  implicit val encoder: Encoder[WebhookData] = deriveEncoder[WebhookData]
}

object EnvelopeSummary {
  implicit val decoder: Decoder[EnvelopeSummary] = deriveDecoder[EnvelopeSummary]
  implicit val encoder: Encoder[EnvelopeSummary] = deriveEncoder[EnvelopeSummary]
}

object DocuSignSender {
  implicit val decoder: Decoder[DocuSignSender] = deriveDecoder[DocuSignSender]
  implicit val encoder: Encoder[DocuSignSender] = deriveEncoder[DocuSignSender]
}

object Recipients {
  implicit val decoder: Decoder[Recipients] = deriveDecoder[Recipients]
  implicit val encoder: Encoder[Recipients] = deriveEncoder[Recipients]
}

object Signer {
  implicit val decoder: Decoder[Signer] = deriveDecoder[Signer]
  implicit val encoder: Encoder[Signer] = deriveEncoder[Signer]
}

object DocuSignDocument {
  implicit val decoder: Decoder[DocuSignDocument] = deriveDecoder[DocuSignDocument]
  implicit val encoder: Encoder[DocuSignDocument] = deriveEncoder[DocuSignDocument]
}

object DocumentPage {
  implicit val decoder: Decoder[DocumentPage] = deriveDecoder[DocumentPage]
  implicit val encoder: Encoder[DocumentPage] = deriveEncoder[DocumentPage]
}

object CustomFields {
  implicit val decoder: Decoder[CustomFields] = deriveDecoder[CustomFields]
  implicit val encoder: Encoder[CustomFields] = deriveEncoder[CustomFields]
}

object TextCustomField {
  implicit val decoder: Decoder[TextCustomField] = deriveDecoder[TextCustomField]
  implicit val encoder: Encoder[TextCustomField] = deriveEncoder[TextCustomField]
}
