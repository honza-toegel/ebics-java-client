import { ref, onMounted } from 'vue';
import { EbicsVersion } from 'components/models/ebics-version';
import { Bank } from 'components/models/ebics-bank';
import { api } from 'boot/axios';
import useBaseAPI from 'components/base-api';
import useDialogs from 'components/dialogs';

/**
 * Bank Connections composition API for bank connection list operations with backend REST API
 * @returns
 *  banks synchronized list of banks
 *  loadBanks function to trigger refreshing of banks
 *  deleteBank function to delete bank
 */
export default function useBanksAPI(avoidRefreshOnMounted = false) {
  const { apiErrorHandler } = useBaseAPI();
  const { confirmDialog } = useDialogs();

  const banks = ref<readonly Bank[]>([]);

  const loadBanks = async (): Promise<void> => {
    try {
      const response = await api.get<Bank[]>('banks');
      banks.value = response.data;
    } catch (error) {
      apiErrorHandler('Loading of banks failed', error);
    }
  };

  const deleteBank = async (bankId: number, bankName: string, askForConfimation = true): Promise<void> => {
    try {
      const canDelete = askForConfimation ? await confirmDialog('Confirm deletion', `Do you really want to delete bank : '${bankName}'`) : true
      if (canDelete) {
        await api.delete<Bank>(`banks/${bankId}`);
        await loadBanks();
      }
    } catch (error) {
      apiErrorHandler('Deleting of bank failed', error);
    }
  };

  const isEbicsVersionAllowedForUse = (bank: Bank, ebicsVersion: EbicsVersion) =>
     !bank.ebicsVersions?.length //The ebicsVersions are empty, till first save happen...
          || bank.ebicsVersions?.some(ver => (ver.version == ebicsVersion && ver.isAllowedForUse));

  if (!avoidRefreshOnMounted)
    onMounted(loadBanks);

  return { banks, loadBanks, deleteBank, isEbicsVersionAllowedForUse };
}
