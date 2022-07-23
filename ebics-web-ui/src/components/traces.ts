import { ref, onMounted } from 'vue';
import { api } from 'boot/axios';
import useBaseAPI from './base-api';
import { TraceEntry } from './models/trace';

export default function useTracesAPI() {
  const { apiErrorHandler } = useBaseAPI();

  const traces = ref<TraceEntry[]>();

  const loadTraces = async (): Promise<void> => {
    try {
      const response = await api.get<TraceEntry[]>('traces');
      traces.value = response.data;
    } catch (error) {
      apiErrorHandler('Loading of traces failed', error);
    }
  };

  onMounted(loadTraces);

  return {
    traces,
  };
}
