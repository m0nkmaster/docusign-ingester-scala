package models

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class DocuSignWebhook(
  event: String,
  uri: String,
  retryCount: String,
  configurationId: String,
  apiVersion: String,
  generatedDateTime: String,
  data: WebhookData
)

case class WebhookData(
  accountId: String,
  recipientId: String,
  envelopeId: String,
  envelopeSummary: EnvelopeSummary
)

case class EnvelopeSummary(
  status: String,
  emailSubject: String,
  emailBlurb: String,
  signingLocation: String,
  enableWetSign: String,
  allowMarkup: String,
  allowReassign: String,
  createdDateTime: String,
  lastModifiedDateTime: String,
  statusChangedDateTime: String,
  useDisclosure: String,
  sender: DocuSignSender,
  recipients: Recipients,
  envelopeDocuments: List[DocuSignDocument],
  customFields: CustomFields
)

case class DocuSignSender(
  userName: String,
  userId: String,
  accountId: String,
  email: String
)

case class Recipients(
  signers: List[Signer]
)

case class Signer(
  recipientId: String,
  name: String,
  email: String,
  status: String,
  signedDateTime: String,
  deliveredDateTime: String,
  recipientIPAddress: String
)

case class DocuSignDocument(
  documentId: String,
  documentIdGuid: String,
  name: String,
  `type`: String,
  order: String,
  pages: String,
  display: String,
  includeInDownload: String,
  signerMustAcknowledge: String,
  templateRequired: String,
  authoritative: String,
  PDFBytes: String // Assuming this is base64 encoded
)

case class CustomFields(
  textCustomFields: List[TextCustomField]
)

case class TextCustomField(
  name: String,
  value: String
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

object CustomFields {
  implicit val decoder: Decoder[CustomFields] = deriveDecoder[CustomFields]
  implicit val encoder: Encoder[CustomFields] = deriveEncoder[CustomFields]
}

object TextCustomField {
  implicit val decoder: Decoder[TextCustomField] = deriveDecoder[TextCustomField]
  implicit val encoder: Encoder[TextCustomField] = deriveEncoder[TextCustomField]
}
